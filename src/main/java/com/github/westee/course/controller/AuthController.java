package com.github.westee.course.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.github.westee.course.configuration.Config;
import com.github.westee.course.dao.UserRepository;
import com.github.westee.course.model.HttpException;
import com.github.westee.course.model.Session;
import com.github.westee.course.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/session")
    public Session authStatus(){
         User currentUser = Config.UserContext.getCurrentUser();
         if(currentUser == null){
           throw new HttpException(401, "未认证");
         } else {
             Session session = new Session();
             session.setUser(currentUser);
            return session;
         }
    }

    @PostMapping("/user")
    public User register(@RequestParam("username") String username,
                         @RequestParam("password") String password) {
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
            throw new HttpException(409, "用户名已经被注册");
        }
        return user;
    }
}
