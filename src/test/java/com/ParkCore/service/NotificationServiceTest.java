package com.ParkCore.service;

import com.ParkCore.enums.NotificationStatus;
import com.ParkCore.exceptions.BadRequestException;
import com.ParkCore.model.Event;
import com.ParkCore.model.Notification;
import com.ParkCore.model.Visitor;
import com.ParkCore.repository.EventRepository;
import com.ParkCore.repository.NotificationRepository;
import org.hibernate.ObjectNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventService eventService;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    public void shouldNotifyParticipantsWhenEventExists() {
        var event = mock(Event.class);

        given(event.getId()).willReturn(1L);
        given(event.getName()).willReturn("Gabby");
        given(event.getEventDate()).willReturn(LocalDate.ofEpochDay(20/11/2024));

        var visitor = mock(Visitor.class);

        given(visitor.getEmail()).willReturn("gabby@gmail.com");

        var notify = mock(Notification.class);

        given(notify.getId()).willReturn(1L);
        given(notify.getName()).willReturn("Gabby");
        given(notify.getSendDate()).willReturn(LocalDate.ofEpochDay(20/11/2024));
        given(notify.getStatus()).willReturn(NotificationStatus.SENT("You have been notified about the event: Gabby"));

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notify);

        var result = notificationService.notifyParticipants(event.getId());

        assertNotNull(result);
        assertEquals("Notifications sent", result);

        verify(eventRepository, times(1)).findById(1L);
    }
    @Test
    public void shouldReturnEventNotFoundWhenEventDoesNotExist(){
        var event = mock(Event.class);

        thenThrownBy(() -> notificationService.notifyParticipants(event.getId())).isInstanceOf(ObjectNotFoundException.class);
    }
}
