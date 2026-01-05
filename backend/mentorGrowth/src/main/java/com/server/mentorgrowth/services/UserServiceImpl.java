package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.UserRequest;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.exceptions.UserAlreadyExistException;
import com.server.mentorgrowth.models.User;
import com.server.mentorgrowth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.server.mentorgrowth.utils.Mapper.map;
import static com.server.mentorgrowth.utils.Validator.validateUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponse saveUser(UserRequest request){
        validateUser(request);

        if(userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistException("User already exists");
        }

        User user = userRepository.save(map(request));
        return map(user);
    }
}
