package com.github.westee.course.controller;

import com.github.westee.course.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class UserController {
    @GetMapping("/user")
    public PageResponse<User> getAllUsers(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "pageSize", required = false)String pageSize,
            @RequestParam(value = "pageNum", required = false)String pageNum,
            @RequestParam(value = "orderBy", required = false)String orderBy,
            @RequestParam(value = "orderType", required = false)String orderType
    ){
        return null;
    }
}
