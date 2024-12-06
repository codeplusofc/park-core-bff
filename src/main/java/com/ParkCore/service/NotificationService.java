package com.ParkCore.service;

import com.ParkCore.model.Event;
import com.ParkCore.model.Notification;
import com.ParkCore.model.Visitor;
import com.ParkCore.repository.EventRepository;
import com.ParkCore.repository.NotificationRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import static com.ParkCore.enums.NotificationStatus.SENT;
import static java.time.LocalDate.now;

@Service
public class NotificationService {

	private final EventRepository eventRepository;
	private final EmailService emailService;
	private final NotificationRepository notificationRepository;

	public NotificationService(NotificationRepository notificationRepository, EventRepository eventRepository, EmailService emailService, NotificationRepository notificationRepository1) {
		this.eventRepository = eventRepository;
		this.emailService = emailService;
		this.notificationRepository = notificationRepository1;
	}

	public String notifyParticipants(Long eventId) {
		var event = eventRepository.findById(eventId);

		if (event.isEmpty()) {
			throw new ObjectNotFoundException("Event not found", Event.class);
		}

		String message = "You have been notified about the event: " + event.get().getName();

		event.get().getConfirmedParticipants().forEach(participant -> {
			sendNotification(participant, event.get(), message);
		});

		return "Notifications sent";
	}

	private void sendNotification(Visitor visitor, Event event, String message) {
		emailService.sendEmailText(visitor.getEmail(), "Event Notification", message);

		saveNotification(visitor, event);
	}

	private void saveNotification(Visitor visitor, Event event) {
		var notification = new Notification();
		notification.setName("Notification for event: " + event.getName());
		notification.setSendDate(now());
		notification.setStatus(SENT);
		notification.setVisitor(visitor);
		notification.setEvent(event);

		notificationRepository.save(notification);
	}
}
