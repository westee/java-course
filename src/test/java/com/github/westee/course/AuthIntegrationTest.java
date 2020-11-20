package com.github.westee.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.westee.course.configuration.Config;
import com.github.westee.course.model.Session;
import com.github.westee.course.model.User;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CourseApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:test.properties"})
public class AuthIntegrationTest {
    @Autowired
    Environment environment;

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    HttpClient client = HttpClient.newHttpClient();
    ObjectMapper objectMapper = new ObjectMapper();

    public String getPort() {
        return environment.getProperty("local.server.port");
    }

    @BeforeEach
    public void resetDatabase() {
        ClassicConfiguration configuration = new ClassicConfiguration();
        configuration.setDataSource(databaseUrl, databaseUsername, databasePassword);
        Flyway flyway = new Flyway(configuration);
        flyway.clean();
        flyway.migrate();
    }

    private HttpResponse<String> post(String url,
                                      String accept,
                                      String contentType,
                                      String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Accept", accept)
                .header("Content-type", contentType)
                .uri(URI.create("http://localhost:" + getPort() + "/api/v1" + url))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> get(String url, String cookie) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Accept", APPLICATION_JSON_VALUE)
                .header("Cookie", cookie)
                .uri(URI.create("http://localhost:" + getPort() + "/api/v1" + url))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> delete(String url, String cookie) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .header("Accept", APPLICATION_JSON_VALUE)
                .header("Cookie", cookie)
                .uri(URI.create("http://localhost:" + getPort() + "/api/v1" + url))
                .DELETE()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @Test
    public void registerLoginLogout() throws IOException, InterruptedException {
        String body = "username=aaaaaa&password=bbbbbb";

        // 注册
        HttpResponse<String> response = post("/user", APPLICATION_JSON_VALUE, APPLICATION_FORM_URLENCODED_VALUE, body);
        User responseUser = objectMapper.readValue(response.body(), User.class);

        assertEquals(201, response.statusCode());
        assertEquals("aaaaaa", responseUser.getUsername());

        // 登录
        response = post("/session", APPLICATION_JSON_VALUE, APPLICATION_FORM_URLENCODED_VALUE, body);
        responseUser = objectMapper.readValue(response.body(), User.class);
        assertEquals(responseUser.getUsername(), "aaaaaa");

        String cookie = response.headers().firstValue("Set-Cookie").get();

        response = get("/session", cookie);
        assertEquals(200, response.statusCode());
        Session session = objectMapper.readValue(response.body(), Session.class);
        assertEquals("aaaaaa", session.getUser().getUsername());

        response = delete("/session", cookie);
        assertEquals(204, response.statusCode());

        response = get("/session", cookie);
        assertEquals(401, response.statusCode());
    }

    @Test
    public void onlyAdminCanSeeAllUsers() throws IOException, InterruptedException {
        HttpResponse<String> response = get("/admin/user", Config.UserInterceptor.COOKIE_NAME+"=test_user_3");
        assertEquals(200, response.statusCode());
    }

    @Test
    public void notAdminCanNotSeeAllUsers() throws IOException, InterruptedException {
        HttpResponse<String> response = get("/admin/user", Config.UserInterceptor.COOKIE_NAME +"=test_user_1");
        assertEquals(403, response.statusCode());
    }

    public void getErrorIfUsernameExist() {

    }

    public void get401IfNoPermission() {

    }
}
