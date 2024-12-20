package com.ParkCore.dto.attractionDto;

import com.ParkCore.enums.AttractionType;
import lombok.Data;

import java.util.List;

@Data
public class AttractionResponseDTO {

    private Long id;
    private String name;
    private String description;
    private AttractionType type;
    private int maximumCapacity;

    // IDs ou informações básicas de tickets, feedbacks e eventos
    private List<Long> ticketIds;
    private List<Long> feedbackIds;
    private List<Long> eventIds;

    public AttractionResponseDTO(Long id, String name, String description, AttractionType type, int maximumCapacity,
                                 List<Long> ticketIds, List<Long> feedbackIds, List<Long> eventIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.maximumCapacity = maximumCapacity;
        this.ticketIds = ticketIds;
        this.feedbackIds = feedbackIds;
        this.eventIds = eventIds;
    }
}