package com.github.westee.course.service;

import com.github.westee.course.annotation.Admin;
import com.github.westee.course.dao.UserDao;
import com.github.westee.course.model.HttpException;
import com.github.westee.course.model.PageResponse;
import com.github.westee.course.model.RoleDao;
import com.github.westee.course.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


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
        if (orderBy != null && orderType == null) {
            orderType = "asc";
        }
        Pageable pageable = null;
        if (orderBy == null) {
            pageable = PageRequest.of(pageNum, pageSize);
        } else {
            pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.fromString(orderType), orderBy));
        }

        if (StringUtils.isEmpty(search)) {
            Page<User> users = userDao.findAll(pageable);
            return new PageResponse<>(users.getTotalPages(), pageNum, pageSize, users.getContent());
        } else {
            var users = userDao.findBySearch(search, pageable);
            return new PageResponse<>(users.getTotalPages(), pageNum, pageSize, users.getContent());
        }
    }

    @Admin
    public User getUser(Integer id) {
        return findById(id);
    }
}