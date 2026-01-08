package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.UserRequest;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.models.User;

import javax.management.relation.Role;
import java.util.List;

public interface UserService {

    UserResponse saveUser(UserRequest request);
    UserResponse findById(String id);
    User findByEmail(String email);
    List<UserResponse> findAllUsers();
    List<UserResponse> getAllMentors();
    List<UserResponse> getAllMentees();
}
