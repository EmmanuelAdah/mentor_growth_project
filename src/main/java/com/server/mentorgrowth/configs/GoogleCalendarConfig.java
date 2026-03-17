//package com.server.mentorgrowth.config;
//
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.services.calendar.Calendar;
//import com.google.auth.http.HttpCredentialsAdapter;
//import com.google.auth.oauth2.GoogleCredentials;
//import lombok.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//
//@Configuration
//public class GoogleCalendarConfig {
//
//    @Bean
//    public Calendar googleCalendar(@Value("${google.credentials}") Resource credentialsResource) throws Exception {
//        GoogleCredentials credentials = GoogleCredentials
//                .fromStream(credentialsResource.getInputStream())
//                .createScoped("https://www.googleapis.com/auth/calendar");
//
//        return new Calendar.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                JacksonFactory.getDefaultInstance(),
//                new HttpCredentialsAdapter(credentials)
//        ).setApplicationName("MentorGrowth").build();
//    }
//}
