package com.github.westee.course.configuration;

import com.github.westee.course.dao.SessionDao;
import com.github.westee.course.model.Session;
import com.github.westee.course.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Stream;

@Configuration
public class Config {
    public static class UserContext{

        private static ThreadLocal<User> currentUser;

        public static User getCurrentUser() {
            return currentUser.get();
        }

        public static void setCurrentUser(User currentUser){
            UserContext.currentUser.set(currentUser);
        }
    }


    public static class UserInterceptor implements HandlerInterceptor {
        public static  final String COOKIE_NAME = "SUN_SESSION_ID";

        @Autowired
        private SessionDao sessionDao;

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            Cookie[] cookies = request.getCookies();
            Stream.of(cookies).filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                    .map(Cookie::getValue)
                    .findFirst()
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
