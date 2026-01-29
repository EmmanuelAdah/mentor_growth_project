package com.server.mentorgrowth.security;

import com.server.mentorgrowth.filter.JwtFilterChain;
import com.server.mentorgrowth.services.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilterChain jwtFilterChain;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                authorizeRequests -> authorizeRequests
                                .requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/swagger-ui.html")
                                .permitAll()
                        .requestMatchers("/api/mentorship/**")
                        .hasAnyRole("MENTOR", "ADMIN")
                        .requestMatchers("/api/notification")
                        .hasRole("ADMIN")
                        .requestMatchers("/api/v1/payment")
                        .hasRole("MENTEE")
                                .anyRequest()
                                .authenticated())
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtFilterChain, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2.loginPage("/login/google")
                        .defaultSuccessUrl("/google/oauth2/authenticated")
                        .failureUrl("/login?error=true"))
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID"))
                .build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
