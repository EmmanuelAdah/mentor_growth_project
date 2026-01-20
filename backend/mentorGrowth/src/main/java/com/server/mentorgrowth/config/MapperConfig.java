package com.server.mentorgrowth.config;

import com.server.mentorgrowth.dtos.response.UserResponse;
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
        modelMapper.typeMap(User.class, UserResponse.class)
                .addMappings(mapper ->
                        mapper.map(
                                user -> user.getRole() != null ? user.getRole().name() : null,
                                UserResponse::setRole
                        )
                );

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setSkipNullEnabled(true);

        return modelMapper;
    }
}
