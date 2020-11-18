package com.github.westee.course.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.github.westee.course.configuration.Config;
import com.github.westee.course.dao.SessionDao;
import com.github.westee.course.dao.UserRepository;
import com.github.westee.course.model.HttpException;
import com.github.westee.course.model.Session;
import com.github.westee.course.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.UUID;

import static com.github.westee.course.configuration.Config.UserInterceptor.COOKIE_NAME;


@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private BCrypt.Verifyer verifyer = BCrypt.verifyer();

    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionDao sessionDao;

    @GetMapping("/session")
    public Session authStatus() {
        User currentUser = Config.UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new HttpException(401, "未认证");
        } else {
            Session session = new Session();
            session.setUser(currentUser);
            return session;
        }
    }

    @PostMapping("/user")
    public User register(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         HttpServletResponse response) {
        if (StringUtils.isEmpty(username) || username.length() > 20 || username.length() < 6) {
            throw new HttpException(400, "用户名必须在6到20之间");
        }
        if (StringUtils.isEmpty(password)) {
            throw new HttpException(400, "密码不能为空");
        }

        User user = new User();
        user.setUsername(username);
        // 1 数据库绝对不能明文存密码
        // 2 不要自己设计加密算法
        user.setEncrypted_password(BCrypt.withDefaults()
                .hashToString(12, password.toCharArray()));
        try {
            userRepository.save(user);
        } catch (Throwable e) {
            // 如果用户名已经被注册
            throw new HttpException(409, e.getMessage());
        }
        response.setStatus(201);
        return user;
    }

    @PostMapping("/session")
    public User login(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      HttpServletResponse response) {
        User user = userRepository.findUsersByUsername(username);
        if (user == null) {
            throw new HttpException(401, "登录失败！");
        } else {
            if (verifyer.verify(password.toCharArray(), user.getEncrypted_password()).verified) {
                String cookie = UUID.randomUUID().toString();

                Session session = new Session();
                session.setCookie(cookie);
                session.setUser(user);
                sessionDao.save(session);

                response.addCookie(new Cookie(COOKIE_NAME, cookie));
                return user;
            } else {
                throw new HttpException(401, "登录失败！");
            }
        }
    }

    @Transactional
    @DeleteMapping("/session")
    public void logout(HttpServletRequest request,
                       HttpServletResponse response) {
        if (Config.UserContext.getCurrentUser() == null) {
            throw new HttpException(401, "未登录");
        }

        Config.getCookie(request).ifPresent(sessionDao::deleteByCookie);
        Cookie cookie = new Cookie(COOKIE_NAME, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.setStatus(204);
    }
}
