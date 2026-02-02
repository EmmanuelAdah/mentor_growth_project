package com.server.mentorgrowth.config;

import com.server.mentorgrowth.dtos.response.*;
import com.server.mentorgrowth.models.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.config.Configuration.AccessLevel;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE);

        modelMapper.typeMap(User.class, UserResponse.class);
        modelMapper.createTypeMap(Notification.class, NotificationResponse.class);
        modelMapper.createTypeMap(Session.class, SessionResponse.class);
        modelMapper.createTypeMap(Review.class, ReviewResponse.class);
        modelMapper.createTypeMap(Mentee.class, User.class);
        modelMapper.createTypeMap(Mentor.class, User.class);
        modelMapper.createTypeMap(Mentorship.class, MentorshipResponse.class);
        modelMapper.createTypeMap(UserResponse.class, User.class);

        modelMapper.typeMap(Payment.class, PaymentResponse.class)
                .addMappings(mapper -> {
                    mapper.map(payment -> payment.getMentee() != null ? payment.getMentee().getId() : null,
                            PaymentResponse::setUserId);
                    mapper.map(payment -> payment.getMentor() != null ? payment.getMentor().getId() : null,
                            PaymentResponse::setMentorId);
                });
        modelMapper.createTypeMap(Goal.class, GoalResponse.class)
                .addMappings(mapper -> {
                    mapper.map(goal -> goal.getMentorship() != null ? goal.getMentorship().getId() : null,
                            GoalResponse::setMentorshipId);
                });

        return modelMapper;
    }
}
