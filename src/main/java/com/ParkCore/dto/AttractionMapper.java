package com.ParkCore.dto;

import com.ParkCore.model.Attraction;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AttractionMapper {

    public Attraction toEntity(AttractionRequestDTO dto) {
        Attraction attraction = new Attraction();
        attraction.setName(dto.getName());
        attraction.setDescription(dto.getDescription());
        attraction.setType(dto.getType());
        attraction.setMaximumCapacity(dto.getMaximumCapacity());
        return attraction;
    }

    public AttractionResponseDTO toResponseDTO(Attraction entity) {
        return new AttractionResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getType(),
                entity.getMaximumCapacity(),
                entity.getTickets() != null ? entity.getTickets().stream().map(ticket -> ticket.getId()).collect(Collectors.toList()) : null,
                entity.getFeedbacks() != null ? entity.getFeedbacks().stream().map(feedback -> feedback.getId()).collect(Collectors.toList()) : null,
                entity.getEvents() != null ? entity.getEvents().stream().map(event -> event.getId()).collect(Collectors.toList()) : null
        );
    }
}
