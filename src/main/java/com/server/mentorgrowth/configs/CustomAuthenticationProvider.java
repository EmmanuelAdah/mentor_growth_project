//package com.server.mentorgrowth.config;
//
//import com.server.mentorgrowth.exceptions.InvalidCredentialsException;
//import com.server.mentorgrowth.models.User;
//import com.server.mentorgrowth.services.UserDetailsServiceImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.Objects;
//
//@Component
//@RequiredArgsConstructor
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//    private final UserDetailsServiceImpl userDetailsService;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public Authentication authenticate(Authentication authentication)
//            throws AuthenticationException {
//
//        String username = authentication.getName();
//        String rawPassword = Objects.requireNonNull(authentication.getCredentials()).toString();
//
//        User user = userDetailsService.loadUserByUsername(username);
//
//        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
//            throw new InvalidCredentialsException("Invalid username or password");
//        }
//
//        return new UsernamePasswordAuthenticationToken(
//                user,
//                null,
//                user.getAuthorities()
//        );
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}
