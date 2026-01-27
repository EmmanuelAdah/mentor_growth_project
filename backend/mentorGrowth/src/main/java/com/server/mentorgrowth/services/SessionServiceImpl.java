package com.server.mentorgrowth.services;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.server.mentorgrowth.dtos.requests.SessionRequest;
import com.server.mentorgrowth.dtos.response.SessionResponse;
import com.server.mentorgrowth.exceptions.NotExistingMentorshipException;
import com.server.mentorgrowth.models.Session;
import com.server.mentorgrowth.repositories.SessionRepository;
import com.server.mentorgrowth.services.interfaces.SessionService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.UUID;
import static com.server.mentorgrowth.utils.Mapper.mapSession;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final MentorshipServiceImpl mentorshipService;
    private final ModelMapper modelMapper;

    @Value("${google.credentials}")
    private String googleCredentials;

    public @Nullable SessionResponse createSession(SessionRequest request) {
        Boolean isMentorshipExist = mentorshipService.isMentorshipExist(request.getMentorshipId());

        if (!isMentorshipExist) {
            throw new NotExistingMentorshipException("No existing mentorship relation found");
        }

        String meetUrl;

        try {
            Event event = new Event()
                    .setSummary("Mentorship Session")
                    .setVisibility("private")
                    .setGuestsCanInviteOthers(false)
                    .setGuestsCanSeeOtherGuests(false)
                    .setStart(new EventDateTime()
                            .setDateTime(new DateTime("2026-02-01T22:00:00+01:00"))) // set the start time
                    .setEnd(new EventDateTime()
                            .setDateTime(new DateTime("2026-02-01T23:00:00+01:00"))) // set the end time
                    .setConferenceData(
                            new ConferenceData().setCreateRequest(
                                    new CreateConferenceRequest()
                                            .setRequestId(UUID.randomUUID().toString())
                                            .setConferenceSolutionKey(
                                                    new ConferenceSolutionKey()
                                                            .setType("hangoutsMeet")
                                            )
                            )
                    );
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new FileInputStream(googleCredentials))
                    .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

            HttpRequestInitializer requestInitializer =
                    new HttpCredentialsAdapter(credentials);

            Calendar calendar = new Calendar.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    requestInitializer
            )
                    .setApplicationName("Mentor Growth")
                    .build();

            Event created = calendar.events()
                    .insert("primary", event)
                    .setConferenceDataVersion(1)
                    .setSendUpdates("none")
                    .execute();

            meetUrl = created.getConferenceData()
                    .getEntryPoints()
                    .stream()
                    .filter(e -> "video".equals(e.getEntryPointType()))
                    .findFirst()
                    .orElseThrow()
                    .getUri();
        } catch (IOException | GeneralSecurityException exception) {
            throw new RuntimeException(exception);
        }

        request.setMeetingLink(meetUrl);
        Session savedSession = sessionRepository.save(mapSession(request));
        return modelMapper.map(savedSession, SessionResponse.class);
    }
}
