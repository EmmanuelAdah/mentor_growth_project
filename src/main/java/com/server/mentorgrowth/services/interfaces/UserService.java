package com.server.mentorgrowth.services.interfaces;

import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.models.User;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface UserService {

    UserResponse findById(String id);
    User findByEmail(String email);
    List<UserResponse> findAllUsers();
    Map<String, UserResponse> findAndMapUsersByIds(List<String> ids, String userId, String mentorId);
    List<UserResponse> getAllMentors();
    List<UserResponse> getAllMentees();
    UserResponse updateProfilePicture(String id, MultipartFile file);
}
