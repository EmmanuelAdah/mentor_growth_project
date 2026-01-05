package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.UserRequest;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.exceptions.InvalidEmailFormatException;
import com.server.mentorgrowth.exceptions.InvalidRoleException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void saveUser_validCredentials() {
        UserRequest request = new UserRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("edo02@gmail.com");
        request.setRole("MENTOr");

        UserResponse savedUser = userService.saveUser(request);
        assertNotNull(savedUser);
        assertEquals(request.getFirstName().toUpperCase(), savedUser.getFirstName());
    }

    @Test
    void saveUser_invalidEmail() {
        UserRequest request = new UserRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("edo02@gmail");
        request.setRole("MENTOr");

        assertThrows(InvalidEmailFormatException.class, () -> userService.saveUser(request));
    }

    @Test
    void saveUser_invalidRole() {
        UserRequest request = new UserRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("edo02@gmail.com");
        request.setRole("Mentoring");

        assertThrows(InvalidRoleException.class, () -> userService.saveUser(request));
    }
}