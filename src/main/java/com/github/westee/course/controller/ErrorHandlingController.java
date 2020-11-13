package com.github.westee.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.westee.course.model.HttpException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandlingController {
    ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler({HttpException.class})
    public void handlerError(HttpServletResponse response, HttpException ex) throws Exception {
        response.setStatus(ex.getStatusCode());
        response.setHeader("content-type", "application/json;charset=UTF-8");
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("message", ex.getMessage());
        response.getOutputStream().write(objectMapper.writeValueAsBytes(jsonObject));
        response.getOutputStream().flush();
    }
}
