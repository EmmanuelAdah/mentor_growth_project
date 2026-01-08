package com.server.mentorgrowth.utils;

import com.server.mentorgrowth.dtos.requests.UserRequest;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.exceptions.InvalidRoleException;
import com.server.mentorgrowth.models.Role;
import com.server.mentorgrowth.models.User;

public class Mapper {

    public static User map(UserRequest request){
        User user = new User();
        user.setFirstName(request.getFirstName()
                .toUpperCase().trim());
        user.setLastName(request.getLastName()
                .toUpperCase().trim());
        user.setEmail(request.getEmail());
        try {
            user.setRole(Role.valueOf(request.getRole()
                    .toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException("Invalid role");
        }
        user.setPassword(request.getPassword());

        return user;
    }

    public static UserResponse map(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());

        return userResponse;
    }
}
