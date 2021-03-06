package com.github.westee.course.configuration;

import com.github.westee.course.dao.SessionDao;
import com.github.westee.course.model.HttpException;
import com.github.westee.course.model.Session;
import com.github.westee.course.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.stream.Stream;

import static com.github.westee.course.configuration.Config.UserInterceptor.COOKIE_NAME;

@Configuration
public class Config implements WebMvcConfigurer {
    @Autowired
    SessionDao sessionDao;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor(sessionDao));
    }

    public static class UserContext {

        private static ThreadLocal<User> currentUser = new ThreadLocal<>();

        public static User getCurrentUser() {
            return currentUser.get();
        }

        public static void setCurrentUser(User currentUser) {
            UserContext.currentUser.set(currentUser);
        }

        public static User getCurrentUserOr401() {
            User user = currentUser.get();
            if (user == null) {
                throw new HttpException(401, "Not login");
            }
            return user;
        }
    }

    public static Optional<String> getCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        Cookie[] cookies = request.getCookies();
        return Stream.of(cookies).filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                .map(Cookie::getValue)
                .findFirst();
    }

    public static class UserInterceptor implements HandlerInterceptor {
        public static final String COOKIE_NAME = "SUN_SESSION_ID";

        SessionDao sessionDao;

        public UserInterceptor(SessionDao sessionDao) {
            this.sessionDao = sessionDao;
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            getCookie(request)
                    .flatMap(sessionDao::findByCookie)
                    .map(Session::getUser)
                    .ifPresent(UserContext::setCurrentUser);
            return true;
        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            UserContext.setCurrentUser(null);
        }
    }
}
