package com.github.westee.course.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class LoginController {
    public static class UsernameAndPassword{
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @PostMapping("/login")
    public Object login(@RequestBody UsernameAndPassword usernameAndPassword, HttpServletResponse httpServletResponse){
        Map<String, String> loginInfo = new ConcurrentHashMap<>();
        Map<String, String> cookieToUsername = new ConcurrentHashMap<>();
        {
            loginInfo.put("laowang", "123456");
            loginInfo.put("password", "123456");
        }
        String password = usernameAndPassword.getPassword();
        String username = usernameAndPassword.getUsername();
        if(password.equals(loginInfo.get(username))){
            String sessionId = UUID.randomUUID().toString();
            String cookieName = "CourseSessionId";

            httpServletResponse.addCookie(new Cookie(cookieName, sessionId));
            cookieToUsername.put(sessionId, username);
            return "login success";
        } else {
            return "login fail";
        }
    }
}
