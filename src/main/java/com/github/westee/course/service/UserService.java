package com.github.westee.course.service;

import com.github.westee.course.annotation.Admin;
import com.github.westee.course.controller.PageResponse;
import com.github.westee.course.dao.UserDao;
import com.github.westee.course.model.HttpException;
import com.github.westee.course.model.RoleDao;
import com.github.westee.course.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    RoleDao roleDao;

    private User findById(Integer id) {
        return userDao.findById(id).orElseThrow(() -> new HttpException(404, "用户不存在！"));
    }

    @Admin
    public User updateUser(Integer id, User user) {

        return null;
    }

    @Admin
    public PageResponse<User> getAllUsers(String search, Integer pageSize, Integer pageNum, String orderBy,
                                          String orderType) {
        return null;
    }

    @Admin
    public User getUser(Integer id) {
        return findById(id);
    }
}