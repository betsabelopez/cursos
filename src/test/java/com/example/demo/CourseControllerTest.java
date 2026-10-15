package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.model.Course;
import com.example.demo.model.CourseLevel;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
class CourseControllerTest {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    @Order(1)
    void shouldReturnCourses() {
        Course[] courses = restTemplate.getForObject("http://localhost:" + port + "/api/courses", Course[].class);

        assertThat(courses).isNotNull();
        assertThat(courses).hasSizeGreaterThanOrEqualTo(3);
    }

    @Test
    @Order(2)
    void shouldCreateCourse() {
        Course request = new Course(null, "Spring Security", "Auth y seguridad", "Backend", CourseLevel.INTERMEDIATE, "Ana", 12);

        ResponseEntity<Course> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/courses",
                new HttpEntity<>(request),
                Course.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Spring Security");
    }
}
