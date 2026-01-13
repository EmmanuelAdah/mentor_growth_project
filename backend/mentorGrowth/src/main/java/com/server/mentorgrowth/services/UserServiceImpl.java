package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.exceptions.UserAlreadyExistException;
import com.server.mentorgrowth.exceptions.UserNotFoundException;
import com.server.mentorgrowth.models.Role;
import com.server.mentorgrowth.models.User;
import com.server.mentorgrowth.repositories.UserRepository;
import com.server.mentorgrowth.utils.Mapper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;
import static com.server.mentorgrowth.utils.Mapper.map;
import static com.server.mentorgrowth.utils.Validator.validateUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserResponse findById(String id){
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserResponse.class))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
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
        List<User> mentors = userRepository.findByRole(Role.ROLE_MENTOR);

        if(mentors.isEmpty()) throw new UserNotFoundException("User not found");

        return mentors.stream()
                .map(Mapper::map)
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
        List<User> mentees = userRepository.findByRole(Role.ROLE_MENTEE);

        if(mentees.isEmpty()) throw new UserNotFoundException("User not found");

        return mentees.stream()
                .map(Mapper::map)
                .toList();
    }

    public void uploadProfilePicture(MultipartFile file){

    }
}
