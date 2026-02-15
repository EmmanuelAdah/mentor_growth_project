package com.server.mentorgrowth.services;

import com.cloudinary.Cloudinary;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.exceptions.UserNotFoundException;
import com.server.mentorgrowth.models.Role;
import com.server.mentorgrowth.models.User;
import com.server.mentorgrowth.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(String.valueOf(UUID.fromString("7e5b7ff1-4445-4c67-9cdd-6297c4e4886e")));
        user.setFirstName("JOHN");
        user.setLastName("DOE");
        user.setRole(Role.ROLE_MENTOR);
        user.setEmail("test@example.com");
        user.setPassword("encoded-password");
    }

    @Test
    void findById_returnsUser() {
        new UserResponse();
        UserResponse expectedResponse = UserResponse.builder()
                .id(String.valueOf(
                        UUID.fromString("7e5b7ff1-4445-4c67-9cdd-6297c4e4886e"))
                )
                .firstName("JOHN")
                .lastName("DOE")
                .email("test@example.com")
                        .build();

        when(userRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(modelMapper.map(user, UserResponse.class)).thenReturn(expectedResponse);

        UserResponse response = userServiceImpl.findById("id");

        assertNotNull(response);
        assertEquals("7e5b7ff1-4445-4c67-9cdd-6297c4e4886e", response.getId());
        assertEquals("JOHN", response.getFirstName());
        assertEquals("DOE", response.getLastName());
    }

    @Test
    void findById_returnsUserNotFound() {

        when(userRepository.findById(any(String.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class,
                () -> userServiceImpl.findById("id"),
                "User not found");
    }

    @Test
    void findByEmail_returnsUser() {

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(user));

        User response = userServiceImpl.findByEmail("test_@example.com");

        assertNotNull(response);
        assertEquals("7e5b7ff1-4445-4c67-9cdd-6297c4e4886e", response.getId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("JOHN", response.getFirstName());
        assertEquals("DOE", response.getLastName());
    }

    @Test
    void findByEmail_returnsUserNotFound() {
        when(userRepository.findByEmail(any(String.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class,
                ()-> userServiceImpl.findByEmail("email"),
                "User not found");
    }

    @Test
    void findAllUsers() {
        List<User> users = new ArrayList<>();



        when(userRepository.findAll()).thenReturn(users);

        List<UserResponse> response = userServiceImpl.findAllUsers();
    }

    @Test
    void getAllMentees() {
    }

    @Test
    void updateProfilePicture() {
//        String filePath = "C:\\DELL\\Downloads\\reflection-from-laptop-glasses-dark-background";
    }
}