package com.ParkCore.service;

import com.ParkCore.model.Event;
import com.ParkCore.repository.EventRepository;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;


    public EventService(EventRepository eventRepository, EmailService emailService) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }
}
