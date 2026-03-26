package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.*;
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
                .jsonPath("$.email").isEqualTo("johndoe@gmail.com")
                .jsonPath("$.firstName").isEqualTo("JOHN");
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
    void findByEmail_userDoesNotExist() {
        when(userService.existByEmail("johndoe@gmail.com"))
                .thenReturn(false);

        webTestClient.get()
                .uri("/api/v1/user/email/johndoe@gmail.com")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(false);
    }

    @Test
    void getAllMentors_ShouldReturnList() {
        UserResponse response = UserResponse.builder()
                .id(String.valueOf(UUID.fromString("7e5b7ff1-4281-4c67-9cdd-6297c4e4986e")))
                .firstName("JOHN")
                .lastName("TEE")
                .email("test@example.com")
                .role("mentor")
                .build();

        when(userService.getAllMentors()).thenReturn(List.of(response));

        webTestClient.get()
                .uri("/api/v1/user/mentors/all") // Adjust to your actual mapping
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].firstName").isEqualTo("JOHN")
                .jsonPath("$.length()").isEqualTo(1);
    }

    @Test
    void getAllMentees_ShouldReturnList() {
        UserResponse response = UserResponse.builder()
                .id(String.valueOf(UUID.fromString("7e5b7ff1-4281-4c67-9cdd-6297c4e4986e")))
                .firstName("JAMES")
                .lastName("TEE")
                .email("test@example.com")
                .role("mentor")
                .build();

        when(userService.getAllMentees()).thenReturn(List.of(response));

        webTestClient.get()
                .uri("/api/v1/user/mentees/all")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .hasSize(1);
    }

    @Test
    void updateProfilePicture_ShouldReturnUpdatedUser() {
        // 1. Arrange
        String userId = "7e5b7ff1-4281-4c67-9cdd-6297c4e4986e";
        UserResponse mockResponse = new UserResponse();
        mockResponse.setProfileImageUrl("http://images.com/photo.jpg");

        when(userService.updateProfilePicture(eq(userId), any(MultipartFile.class)))
                .thenReturn(mockResponse);

        // 2. Build the multipart data
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("id", userId, MediaType.TEXT_PLAIN);

        // Build the file part
        builder.part("file", "fake-image-content".getBytes())
                .filename("profile.jpg")
                .contentType(MediaType.IMAGE_JPEG);

        // 3. Act & Assert
        webTestClient.put()
                .uri("/api/v1/user/update/image") // No query params needed here
                .body(BodyInserters.fromMultipartData(builder.build()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.profileImageUrl").isEqualTo("http://images.com/photo.jpg");
    }
}