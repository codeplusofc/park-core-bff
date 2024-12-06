package com.ParkCore.controller;

import com.ParkCore.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/{eventId}/notify-participants")
    public ResponseEntity<String> notifyParticipants(@PathVariable Long eventId) {
        var response = notificationService.notifyParticipants(eventId);
        if (response.equals("Event not found")) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
}
