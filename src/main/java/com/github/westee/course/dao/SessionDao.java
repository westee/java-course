package com.github.westee.course.dao;

import com.github.westee.course.model.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionDao extends CrudRepository<Session, Integer> {
    Optional<Session> findByCookie(String cookie);
}
