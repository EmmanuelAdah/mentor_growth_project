package com.server.mentorgrowth.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

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
        User user1 = new User();
        user.setId(String.valueOf(UUID.fromString("7e5b7ff1-4445-4c67-9cdd-6297c4e4886e")));
        user.setFirstName("JOHN");
        user.setLastName("DOE");
        user.setRole(Role.ROLE_MENTOR);
        user.setEmail("test@example.com");
        user.setPassword("encoded-password");

        User user2 = new User();
        user.setId(String.valueOf(UUID.fromString("7e5b7ff1-4281-4c67-9cdd-6297c4e4986e")));
        user.setFirstName("JAMES");
        user.setLastName("TEE");
        user.setRole(Role.ROLE_MENTEE);
        user.setEmail("test@example.com");
        user.setPassword("encoded-password");

        List<User> users = new ArrayList<>(List.of(user1, user2));

        UserResponse userResponse = UserResponse.builder()
                .id(String.valueOf(UUID.fromString("7e5b7ff1-4281-4c67-9cdd-6297c4e4986e")))
                .firstName("JAMES")
                .lastName("TEE")
                .email("test@example.com")
                .role("mentee")
                .build();

        when(userRepository.findAll()).thenReturn(users);
        when(modelMapper.map(any(User.class), eq(UserResponse.class)))
                .thenReturn(userResponse);

        List<UserResponse> response = userServiceImpl.findAllUsers();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("JAMES", response.get(1).getFirstName());
        assertEquals("TEE", response.get(1).getLastName());
        assertEquals("mentee", response.get(1).getRole());
        verify(modelMapper, times(2)).map(any(User.class), eq(UserResponse.class));
//        verify(userRepository).save(any(User.class));
    }

    @Test
    void getAllMentees() {
        User user1 = new User();
        user.setId(String.valueOf(UUID.fromString("7e5b7ff1-4445-4c67-9cdd-6297c4e4886e")));
        user.setFirstName("JOHN");
        user.setLastName("DOE");
        user.setRole(Role.ROLE_MENTEE);
        user.setEmail("test@example.com");
        user.setPassword("encoded-password");

        User user2 = new User();
        user.setId(String.valueOf(UUID.fromString("7e5b7ff1-4281-4c67-9cdd-6297c4e4986e")));
        user.setFirstName("JAMES");
        user.setLastName("TEE");
        user.setRole(Role.ROLE_MENTEE);
        user.setEmail("test@example.com");
        user.setPassword("encoded-password");

        List<User> users = new ArrayList<>(List.of(user1, user2));

        UserResponse userResponse = UserResponse.builder()
                .id(String.valueOf(UUID.fromString("7e5b7ff1-4281-4c67-9cdd-6297c4e4986e")))
                .firstName("JAMES")
                .lastName("TEE")
                .email("test@example.com")
                .role("mentee")
                .build();

        when(userRepository.findAll()).thenReturn(users);
        when(modelMapper.map(any(User.class), eq(UserResponse.class)))
                .thenReturn(userResponse);

        List<UserResponse> response = userServiceImpl.findAllUsers();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("JAMES", response.get(1).getFirstName());
        assertEquals("TEE", response.get(1).getLastName());
        assertEquals("mentee", response.get(1).getRole());
        assertFalse("mentor".equals(response.get(1).getRole()));

        verify(modelMapper, times(2)).map(any(User.class), eq(UserResponse.class));
    }

    @Test
    void updateProfilePicture_Success() throws IOException {
        // Arrange
        String userId = "7e5b7ff1-4281-4c67-9cdd-6297c4e4986e";
        MultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "image-data".getBytes());

        User user = new User();
        user.setId(userId);

        Map<String, Object> uploadResult = Map.of("secure_url", "https://cloudinary.com/photo.jpg");
        UserResponse expectedResponse = new UserResponse();
        expectedResponse.setProfileImage("https://cloudinary.com/photo.jpg");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(uploadResult);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(any(User.class), eq(UserResponse.class))).thenReturn(expectedResponse);

        UserResponse result = userServiceImpl.updateProfilePicture(userId, mockFile);

        assertNotNull(result);
        assertEquals("https://cloudinary.com/photo.jpg", result.getProfileImage());
        assertEquals(user.getId(), result.getId());

        verify(userRepository).save(user);
        verify(uploader).upload(eq(mockFile.getBytes()), anyMap());
    }

    @Test
    void updateProfilePicture_UserNotFound() {

        String userId = "7e5b7ff1-4281-4c67-9cdd-6297c4e4986e";
        MultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "data".getBytes());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.updateProfilePicture(userId, mockFile));
    }
}