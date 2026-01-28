package com.server.mentorgrowth.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.ByteArrayInputStream;
import java.util.Collections;

import static reactor.netty.http.HttpConnectionLiveness.log;

@Configuration
public class GoogleCalendarConfig {

        @Value("${google.credentials.path}")
        private String googleCredentialsJson;

        @Bean
        public Calendar googleCalendar() throws Exception {
            GoogleCredentials credentials;

            log.info("Using Google credentials from env: {}",
                    System.getenv(googleCredentialsJson));

            if (!googleCredentialsJson.isBlank()) {
                credentials = GoogleCredentials.fromStream(
                        new ByteArrayInputStream(googleCredentialsJson.getBytes())
                ).createScoped(Collections.singleton(CalendarScopes.CALENDAR));
            } else {
                // Production: use Application Default Credentials
                credentials = GoogleCredentials.getApplicationDefault()
                        .createScoped(Collections.singleton(CalendarScopes.CALENDAR));
            }

            return new Calendar.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials)
            )
                    .setApplicationName("Mentor Growth")
                    .build();
        }
}
