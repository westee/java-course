package com.github.westee.course;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

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

    public String getPort(){
        return environment.getProperty("local.server.port");
    }

    @BeforeEach
    public void resetDatabase(){
        ClassicConfiguration configuration = new ClassicConfiguration();
        configuration.setDataSource(databaseUrl, databaseUsername, databasePassword);
        Flyway flyway = new Flyway(configuration);
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void registerLoginLogout() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String body = "username=aaaaaa&password=bbbbbb";

        ObjectMapper objectMapper = new ObjectMapper();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Accept", "application/json")
                .header("Content-type", APPLICATION_FORM_URLENCODED_VALUE)
                .uri(URI.create("http://localhost:" + getPort() + "/api/v1/user" ))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());

        User responseUser = objectMapper.readValue(response.body(), User.class);

        Assertions.assertEquals(201, response.statusCode());
        Assertions.assertEquals("aaa", responseUser.getUsername());
    }

    public void getErrorIfUsernameExist() {

    }

    public void get401IfNoPermission() {

    }
}
