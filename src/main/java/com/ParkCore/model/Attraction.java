package com.ParkCore.model;

import com.ParkCore.enums.AttractionType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Attraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private AttractionType type;

    private int maximumCapacity;

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL)
    private List<Event> events;

    public Attraction(Long id, String name, String description, AttractionType type, int maximumCapacity, List<Ticket> tickets, List<Feedback> feedbacks, List<Event> events) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.maximumCapacity = maximumCapacity;
        this.tickets = tickets;
        this.feedbacks = feedbacks;
        this.events = events;
    }
    public Attraction(){

    }
}
