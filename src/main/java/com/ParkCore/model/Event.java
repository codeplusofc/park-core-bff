package com.ParkCore.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDate eventDate;

    @ManyToOne
    @JoinColumn(name = "attraction_id", nullable = false)
    private Attraction attraction;

    @ElementCollection
    private List<String> requiredResources;

    private String status;

    @ManyToMany(mappedBy = "events")
    private List<Visitor> confirmedParticipants;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    public Event(Long id, String name, String description, LocalDate eventDate, Attraction attraction, List<String> requiredResources, String status, List<Visitor> confirmedParticipants, List<Notification> notifications) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.attraction = attraction;
        this.requiredResources = requiredResources;
        this.status = status;
        this.confirmedParticipants = confirmedParticipants;
        this.notifications = notifications;
    }
    public Event(){

    }
}
