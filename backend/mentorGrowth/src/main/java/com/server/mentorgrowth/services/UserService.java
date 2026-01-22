package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.RegisterRequest;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserResponse findById(String id);
    User findByEmail(String email);
    List<UserResponse> findAllUsers();
    List<UserResponse> getAllMentors();
    List<UserResponse> getAllMentees();
    UserResponse updateProfilePicture(String id, MultipartFile file);
}
