package com.server.mentorgrowth.config;

import com.server.mentorgrowth.dtos.requests.SessionRequest;
import com.server.mentorgrowth.dtos.response.NotificationResponse;
import com.server.mentorgrowth.dtos.response.SessionResponse;
import com.server.mentorgrowth.dtos.response.UserResponse;
import com.server.mentorgrowth.models.Notification;
import com.server.mentorgrowth.models.Session;
import com.server.mentorgrowth.models.User;
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
        modelMapper.createTypeMap(SessionRequest.class, Session.class);
        modelMapper.createTypeMap(Session.class, SessionResponse.class);

        return modelMapper;
    }
}
