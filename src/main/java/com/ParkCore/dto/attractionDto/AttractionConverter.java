package com.ParkCore.dto.attractionDto;

import com.ParkCore.model.Attraction;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
public class AttractionConverter {

    public Attraction toEntity(AttractionRequestDTO attractionRequestDTO) {
        var attraction = new Attraction();
        attraction.setName(attractionRequestDTO.getName());
        attraction.setDescription(attractionRequestDTO.getDescription());
        attraction.setType(attractionRequestDTO.getType());
        attraction.setMaximumCapacity(attractionRequestDTO.getMaximumCapacity());
        return attraction;
    }

    public AttractionResponseDTO toResponseDTO(Attraction attraction) {
        return new AttractionResponseDTO(
                attraction.getId(),
                attraction.getName(),
                attraction.getDescription(),
                attraction.getType(),
                attraction.getMaximumCapacity(),
                attraction.getTickets().stream().map(ticket -> ticket.getId()).collect(toList()),
                attraction.getFeedbacks().stream().map(feedback -> feedback.getId()).collect(toList()),
                attraction.getEvents().stream().map(event -> event.getId()).collect(toList())
        );
    }
}
