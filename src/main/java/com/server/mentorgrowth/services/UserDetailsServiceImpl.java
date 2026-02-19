package com.server.mentorgrowth.services;

import com.server.mentorgrowth.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserServiceImpl userService;

    @Override
    public User loadUserByUsername(String email)  {
        return userService.findByEmail(email);
    }
}
