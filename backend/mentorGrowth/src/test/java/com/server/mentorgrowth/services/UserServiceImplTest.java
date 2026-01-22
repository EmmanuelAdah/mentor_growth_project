package com.server.mentorgrowth.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void findById() {

    }

    @Test
    void findByEmail() {
    }

    @Test
    void findAllUsers() {
    }

    @Test
    void getAllMentors() {
    }

    @Test
    void existByEmail() {
    }

    @Test
    void existById() {
    }

    @Test
    void getAllMentees() {
    }

    @Test
    void updateProfilePicture() {
    }
}