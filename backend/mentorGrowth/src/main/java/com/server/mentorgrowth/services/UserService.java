package com.server.mentorgrowth.services;

import com.server.mentorgrowth.dtos.requests.UserRequest;
import com.server.mentorgrowth.dtos.response.UserResponse;

public interface UserService {

    UserResponse saveUser(UserRequest request);
}
