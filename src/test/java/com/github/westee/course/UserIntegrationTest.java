package com.github.westee.course;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.westee.course.model.PageResponse;
import com.github.westee.course.model.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserIntegrationTest extends AbstractIntegrationTest {
    @Test
    public void get401IfNotLogin() throws IOException, InterruptedException {
        assertEquals(get("/user").statusCode(), 401);
        assertEquals(get("/user?search=a&pageSize=10").statusCode(), 401);
        assertEquals(get("/user/10").statusCode(), 401);
    }

    @Test
    public void get403IfNotLogin() throws IOException, InterruptedException {
        assertEquals(get("/user", studentUserCookie).statusCode(), 403);
        assertEquals(get("/user", teacherUserCookie).statusCode(), 403);
        assertEquals(get("/user/123", studentUserCookie).statusCode(), 403);
        assertEquals(get("/user/123", teacherUserCookie).statusCode(), 403);
        assertEquals(patch("/user/123", "{}", Map.of("Cookie", studentUserCookie)).statusCode(), 403);
        assertEquals(patch("/user/123", "{}", Map.of("Cookie", teacherUserCookie)).statusCode(), 403);

    }

    @Test
    public void adminCanSearchUsers() throws IOException, InterruptedException {
        var body = get("/user?pageSize=1&pageNum=2&orderBy=id&orderType=asc", adminUserCookie).body();
        PageResponse<User> userPageResponse = objectMapper.readValue(body, new TypeReference<>() {});

        assertEquals(2,userPageResponse.getPageNum());
        assertEquals(1,userPageResponse.getPageSize());
        assertEquals(3,userPageResponse.getTotalPage());
        assertEquals("老师",userPageResponse.getData().get(0).getUsername());
    }

    @Test
    public void adminCanGetAllUser() {
    }

    @Test
    public void adminCanGetOneUser() {
    }

    @Test
    public void get404IfUserNotExist() {
    }

    @Test
    public void adminCanUpdateUserRole() {
    }
}
