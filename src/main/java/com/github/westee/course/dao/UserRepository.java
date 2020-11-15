package com.github.westee.course.dao;

import com.github.westee.course.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    @Override
    <S extends User> S save(S s);
}
