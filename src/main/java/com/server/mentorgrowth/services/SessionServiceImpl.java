//package com.server.mentorgrowth.services;
//
//import com.google.api.client.util.DateTime;
//import com.google.api.services.calendar.Calendar;
//import com.google.api.services.calendar.model.*;
//import com.server.mentorgrowth.dtos.requests.SessionRequest;
//import com.server.mentorgrowth.dtos.response.MentorshipResponse;
//import com.server.mentorgrowth.services.interfaces.SessionService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import java.io.IOException;
//import java.util.List;
//import java.util.Objects;
//import java.util.UUID;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class SessionServiceImpl implements SessionService {
//    private final MentorshipServiceImpl mentorshipService;
//    private final Calendar service;
//
//    @Override
//    public String createSession(SessionRequest request) {
//        MentorshipResponse mentorship = mentorshipService.findById(request.getMentorshipId());
//        log.info("Creating session for mentorship: {}", Objects.requireNonNull(mentorship).getId());
//
//        int duration = request.getDurationInMinutes() * 60000;
//
//        try {
//            Event event = new Event()
//                    .setSummary(request.getSummary())
//                    .setDescription("Mentorship Session");
//
//            // Time setting (as discussed)
//            DateTime start = new DateTime(request.getStartTime());
//            event.setStart(new EventDateTime().setDateTime(start).setTimeZone("Africa/Lagos"));
//            event.setEnd(new EventDateTime().setDateTime(new DateTime(start.getValue() + duration)).setTimeZone("Africa/Lagos"));
//
//            // --- Adding parties involved in the session ---
//            List<EventAttendee> attendees = Stream.of(
//                            mentorship.getMentor().getEmail(),
//                            mentorship.getMentee().getEmail()
//                    )
//                    .map(email -> new EventAttendee().setEmail(email))
//                    .collect(Collectors.toList());
//            event.setAttendees(attendees);
//
//            // --- CONFIGURING THE MEET LINK ---
//            ConferenceData conferenceData = new ConferenceData()
//                    .setCreateRequest(new CreateConferenceRequest()
//                            .setRequestId(UUID.randomUUID().toString())
//                            .setConferenceSolutionKey(new ConferenceSolutionKey().setType("hangoutsMeet")));
//            event.setConferenceData(conferenceData);
//
//            Event createdEvent = service.events().insert("primary", event)
//                    .setConferenceDataVersion(1)
//                    // This ensures they get an email notification
//                    .setSendUpdates("all")
//                    .execute();
//
//            return createdEvent.getHangoutLink();
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to create Google Calendar event", e);
//        }
//    }
//}
