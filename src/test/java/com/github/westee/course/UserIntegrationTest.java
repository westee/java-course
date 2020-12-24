package com.github.westee.course;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.westee.course.model.PageResponse;
import com.github.westee.course.model.Role;
import com.github.westee.course.model.User;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        var body = get("/user?search=学生&pageSize=10&pageNum=1&orderBy=id&orderType=asc", adminUserCookie).body();
        System.out.println(body);
        PageResponse<User> userPageResponse = objectMapper.readValue(body, new TypeReference<>() {});

        assertEquals(1,userPageResponse.getPageNum());
        assertEquals(10,userPageResponse.getPageSize());
        assertEquals(1,userPageResponse.getTotalPage());
        assertEquals("学生",userPageResponse.getData().get(0).getUsername());
    }

    @Test
    public void adminCanGetAllUser() throws IOException, InterruptedException {
        var body = get("/user?pageSize=1&pageNum=2&orderBy=id&orderType=asc", adminUserCookie).body();
        System.out.println(body);
        PageResponse<User> userPageResponse = objectMapper.readValue(body, new TypeReference<>() {});

        assertEquals(2,userPageResponse.getPageNum());
        assertEquals(1,userPageResponse.getPageSize());
        assertEquals(3,userPageResponse.getTotalPage());
        assertEquals("老师",userPageResponse.getData().get(0).getUsername());
    }

    @Test
    public void adminCanGetOneUser() {
    }

    @Test
    public void get404IfUserNotExist() {
    }

    @Test
    public void adminCanUpdateUserRole() throws IOException, InterruptedException {
        String response = get("/user/1", adminUserCookie).body();
        User student = objectMapper.readValue(response, User.class);
        Role role  = new Role();
        role.setName("管理员");
        student.getRoles().add(role);

        int statusCode = patch("/user/1", objectMapper.writeValueAsString(student),
                Map.of("Cookie", adminUserCookie)).statusCode();
        assertEquals(200, statusCode);

        User updatedUser = objectMapper.readValue(get("/user/1", adminUserCookie).body(), User.class);

        assertTrue(updatedUser.getRoles().stream().anyMatch(role1 -> role1.getName().equals("管理员")), "管理员");
    }
}
