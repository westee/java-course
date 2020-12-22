package com.github.westee.course.dao;

import com.github.westee.course.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserDao extends CrudRepository<User, Integer> {
    User findUsersByUsername(String username);

    @Query(value = "select u from User u where u.username like %:search%")
    Page<User> findBySearch(@Param("search") String search, Pageable pageable);
}
