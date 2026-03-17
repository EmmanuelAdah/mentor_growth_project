package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.mockito.Mockito.when;

class UserControllerTest {
    UserServiceImpl userService;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserServiceImpl.class);
        UserController controller = new UserController(userService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void findById() {
        UserResponse userResponse = UserResponse.builder()
                .id("7e5b7ff1-4445-4c67-9cdd-6297c4e4886e")
                .firstName("JOHN")
                .lastName("DOE")
                .email("johndoe@gmail.com")
                .build();

        when(userService.findById("7e5b7ff1-4445-4c67-9cdd-6297c4e4886e"))
                .thenReturn(userResponse);

        webTestClient.get()
                .uri("/api/v1/user/7e5b7ff1-4445-4c67-9cdd-6297c4e4886e")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("7e5b7ff1-4445-4c67-9cdd-6297c4e4886e")
                .jsonPath("$.email").isEqualTo("johndoe@gmail.com");
    }

    @Test
    void findByEmail() {
        when(userService.existByEmail("johndoe@gmail.com"))
                .thenReturn(true);

        webTestClient.get()
                .uri("/api/v1/user/email/johndoe@gmail.com")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);
    }

    @Test
    void getAllMentors() {
    }

    @Test
    void getAllMentees() {
    }

    @Test
    void updateProfilePicture() {
    }
}