package com.github.westee.course.dao;

import com.github.westee.course.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Integer> {
    User findUsersByUsername(String username);
}
