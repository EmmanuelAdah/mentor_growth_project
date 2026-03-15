package com.server.mentorgrowth.controllers;

import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.models.User;
import com.server.mentorgrowth.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private ModelMapper mapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("7e5b7ff1-4445-4c67-9cdd-6297c4e4886e");
        user.setFirstName("JOHN");
        user.setLastName("DOE");
        user.setEmail("johndoe@gmail.com");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findById() throws Exception {

        UserResponse userResponse = UserResponse.builder()
                .id("7e5b7ff1-4445-4c67-9cdd-6297c4e4886e")
                .firstName("JOHN")
                .lastName("DOE")
                .email("johndoe@gmail.com")
                .build();

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(user));

        when(mapper.map(user, UserResponse.class))
                .thenReturn(userResponse);

        mockMvc.perform(get("/api/v1/user/7e5b7ff1-4445-4c67-9cdd-6297c4e4886e")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("JOHN"))
                .andExpect(jsonPath("$.email").value("johndoe@gmail.com"));
    }

    @Test
    void findByEmail() {
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