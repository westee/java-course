package com.github.westee.course.controller;

import com.github.westee.course.model.User;
import com.github.westee.course.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    UserService userService;
    @GetMapping("/user")
    public PageResponse<User> getAllUsers(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(value = "orderBy", required = false)String orderBy,
            @RequestParam(value = "orderType", required = false)String orderType
    ){
        if(pageSize == null){
            pageSize = 10;
        }

        if(pageNum == null){
            pageNum = 1;
        }
        return null;
        //        return userService.getAllUsers(search, pageSize, pageNum, orderBy, orderType);
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Integer id){
        return userService.getUser(id);
    }

    @PatchMapping("/user/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User user){
        return userService.updateUser(id, user);
    }
}
