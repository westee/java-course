package com.github.westee.course.annotation;

import com.github.westee.course.dao.UserDao;
import com.github.westee.course.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleManageService {
    @Autowired
    UserDao userRepository;

    @Admin
    public List<User> getAllUsers() {
       return (List<User>) userRepository.findAll();
    }
}
