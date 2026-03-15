package com.server.mentorgrowth.services;

import com.cloudinary.Cloudinary;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.exceptions.UserNotFoundException;
import com.server.mentorgrowth.models.Role;
import com.server.mentorgrowth.models.User;
import com.server.mentorgrowth.repositories.UserRepository;
import com.server.mentorgrowth.services.interfaces.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;

    @Override
    public UserResponse findById(String id){
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserResponse.class))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Invalid email or password"));
    }

    @Override
    public @Nullable List<UserResponse> findAllUsers() {
        return  userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getAllMentors() {
        return userRepository.findByRole(Role.ROLE_MENTOR)
                .stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .toList();
    }

    public Boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Boolean existById(String id) {
        return userRepository.existsById(id);
    }

    @Override
    public @Nullable List<UserResponse> getAllMentees() {
        return userRepository.findByRole(Role.ROLE_MENTEE)
                .stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .toList();
    }

    @Override
    public UserResponse updateProfilePicture(String id, MultipartFile file) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        try {
            var result = cloudinary.uploader().upload(
                    file.getInputStream(),
                    Map.of("folder", "uploads", "resource_type", "image")
            );
            user.setProfileImage(result.get("secure_url").toString());

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed");
        }
        return modelMapper.map(userRepository.save(user), UserResponse.class);
    }

    @Override
    public Map<String, UserResponse> findAndMapUsersByIds(List<String> ids, @NotBlank String userId, @NotBlank String mentorId) {
        List<User> users = userRepository.findAllById(ids);
        if (users.isEmpty())
            throw new UserNotFoundException("No user found");

        List<UserResponse> userResponses = users.stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .toList();

        userResponses.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Mentee not found"));

        userResponses.stream()
                .filter(user -> user.getId().equals(mentorId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Mentor found"));

        return userResponses.stream()
                .collect(Collectors.toMap(UserResponse::getId, user -> user));
    }
}
