package com.server.mentorgrowth.services;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.server.mentorgrowth.dtos.requests.SessionRequest;
import com.server.mentorgrowth.dtos.response.SessionResponse;
import com.server.mentorgrowth.exceptions.InvalidMentorshipException;
import com.server.mentorgrowth.exceptions.ServerConnectionException;
import com.server.mentorgrowth.models.Mentorship;
import com.server.mentorgrowth.models.Session;
import com.server.mentorgrowth.repositories.SessionRepository;
import com.server.mentorgrowth.services.interfaces.MentorshipService;
import com.server.mentorgrowth.services.interfaces.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final MentorshipService mentorshipService;
    private final SessionRepository sessionRepository;
    private final ObjectProvider<Calendar> googleCalendarProvider;

    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Africa/Lagos");

    @Override
    @Transactional
    public SessionResponse createSession(SessionRequest request) {
        Calendar googleCalendar = googleCalendarProvider.getIfAvailable();
        if (googleCalendar == null) {
            throw new ServerConnectionException("Google Calendar is not configured");
        }

        Mentorship mentorship = mentorshipService.getMentorshipEntity(request.getMentorshipId());
        log.info("Creating session for mentorship: {}", mentorship.getId());

        if (mentorship.getMentor() == null || mentorship.getMentee() == null) {
            throw new InvalidMentorshipException("Mentorship is missing mentor or mentee");
        }

        long durationMs = request.getDurationInMinutes() * 60_000L;
        DateTime start = new DateTime(request.getStartTime());
        String meetingLink;

        try {
            Event event = new Event()
                    .setSummary(request.getSummary())
                    .setDescription("Mentorship Session");

            event.setStart(new EventDateTime().setDateTime(start).setTimeZone(DEFAULT_ZONE.getId()));
            event.setEnd(new EventDateTime()
                    .setDateTime(new DateTime(start.getValue() + durationMs))
                    .setTimeZone(DEFAULT_ZONE.getId()));

            List<EventAttendee> attendees = Stream.of(
                            mentorship.getMentor().getEmail(),
                            mentorship.getMentee().getEmail()
                    )
                    .map(email -> new EventAttendee().setEmail(email))
                    .collect(Collectors.toList());
            event.setAttendees(attendees);

            ConferenceData conferenceData = new ConferenceData()
                    .setCreateRequest(new CreateConferenceRequest()
                            .setRequestId(UUID.randomUUID().toString())
                            .setConferenceSolutionKey(new ConferenceSolutionKey().setType("hangoutsMeet")));
            event.setConferenceData(conferenceData);

            Event createdEvent = googleCalendar.events().insert("primary", event)
                    .setConferenceDataVersion(1)
                    .setSendUpdates("all")
                    .execute();

            meetingLink = createdEvent.getHangoutLink();
        } catch (IOException e) {
            throw new ServerConnectionException("Failed to create Google Calendar event");
        }

        LocalDateTime scheduledTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(start.getValue()),
                DEFAULT_ZONE
        );

        Session session = Session.builder()
                .mentorship(mentorship)
                .scheduledTime(scheduledTime)
                .durationMinutes(request.getDurationInMinutes())
                .status("SCHEDULED")
                .meetingLink(meetingLink)
                .build();

        Session saved = sessionRepository.save(session);
        return toResponse(saved);
    }

    private SessionResponse toResponse(Session session) {
        SessionResponse response = new SessionResponse();
        response.setId(session.getId());
        response.setMentorshipId(session.getMentorship().getId());
        response.setUserId(session.getMentorship().getMentor().getId());
        response.setMenteeId(session.getMentorship().getMentee().getId());
        response.setScheduledTime(session.getScheduledTime() != null ? session.getScheduledTime().toString() : null);
        response.setDurationMinutes(session.getDurationMinutes());
        response.setMeetingLink(session.getMeetingLink());
        response.setStatus(session.getStatus());
        response.setCreatedAt(session.getCreatedAt());
        return response;
    }
}
