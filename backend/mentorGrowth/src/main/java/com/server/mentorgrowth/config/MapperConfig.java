package com.server.mentorgrowth.config;

import com.server.mentorgrowth.dtos.response.*;
import com.server.mentorgrowth.models.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    ModelMapper modelMapper = new ModelMapper();

    @Bean
    public ModelMapper modelMapper() {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        modelMapper.typeMap(User.class, UserResponse.class)
                .addMappings(mapper ->
                        mapper.map(
                                user -> user.getRole() != null ? user.getRole().name() : null,
                                UserResponse::setRole
                        )
                );

        modelMapper.createTypeMap(Notification.class, NotificationResponse.class);
        modelMapper.createTypeMap(Session.class, SessionResponse.class);
        modelMapper.createTypeMap(Review.class, ReviewResponse.class);
        modelMapper.createTypeMap(Mentorship.class, MentorshipResponse.class);
        modelMapper.createTypeMap(UserResponse.class, User.class);
        modelMapper.createTypeMap(Payment.class, PaymentResponse.class);

        return modelMapper;
    }
}
