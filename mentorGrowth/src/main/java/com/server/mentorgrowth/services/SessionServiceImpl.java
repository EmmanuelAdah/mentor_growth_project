//package com.server.mentorgrowth.services;
//
//import com.google.api.client.util.DateTime;
//import com.google.api.services.calendar.Calendar;
//import com.google.api.services.calendar.model.*;
//import com.server.mentorgrowth.models.Mentorship;
//import com.server.mentorgrowth.services.interfaces.SessionService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.Objects;
//import java.util.UUID;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class SessionServiceImpl implements SessionService {
//    private final Calendar calendar;
//    private final MentorshipServiceImpl mentorshipService;
//
//    @Override
//    public String createSession(String mentorshipId) {
//        Mentorship mentorship = mentorshipService.findById(mentorshipId);
//        log.info("Creating session for mentorship: {}", Objects.requireNonNull(mentorship).getId());
//        LocalDateTime startTime = LocalDateTime.of(2026, 2, 1, 22, 0);
//        int durationMinutes = 45;
//
//        try {
//            Event event = new Event()
//                    .setSummary("Mentorship Session")
//                    .setVisibility("private")
//                    .setGuestsCanInviteOthers(false)
//                    .setGuestsCanSeeOtherGuests(false)
//                    .setStart(new EventDateTime().setDateTime(new DateTime(startTime.toString())))
//                    .setEnd(new EventDateTime().setDateTime(
//                            new DateTime(startTime.plusMinutes(durationMinutes).toString())
//                    ))
//                    .setConferenceData(new ConferenceData().setCreateRequest(
//                            new CreateConferenceRequest()
//                                    .setRequestId(UUID.randomUUID().toString())
//                                    .setConferenceSolutionKey(
//                                            new ConferenceSolutionKey().setType("hangoutsMeet")
//                                    )
//                    ));
//
//            Event created = calendar.events()
//                    .insert("primary", event)
//                    .setConferenceDataVersion(1)
//                    .execute();
//
//            return created.getConferenceData().getEntryPoints()
//                    .stream()
//                    .filter(e -> "video".equals(e.getEntryPointType()))
//                    .findFirst()
//                    .orElseThrow(() -> new RuntimeException("No video link found"))
//                    .getUri();
//
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to create Google Calendar event", e);
//        }
//    }
//}
