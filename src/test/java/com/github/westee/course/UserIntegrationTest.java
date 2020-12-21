package com.github.westee.course;

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
    public void adminCanSearchUser() {
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
