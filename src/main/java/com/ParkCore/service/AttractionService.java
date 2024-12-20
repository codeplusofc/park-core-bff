package com.ParkCore.service;

import com.ParkCore.dto.attractionDto.AttractionMapper;
import com.ParkCore.dto.attractionDto.AttractionRequestDTO;
import com.ParkCore.dto.attractionDto.AttractionResponseDTO;
import com.ParkCore.enums.AttractionType;
import com.ParkCore.exceptions.BadRequestException;
import com.ParkCore.exceptions.NoContentException;
import com.ParkCore.repository.AttractionRepository;
import com.ParkCore.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttractionService {

    private final AttractionRepository attractionRepository;
    private final EventRepository eventRepository;
    private final AttractionMapper attractionMapper;

    public AttractionService(AttractionRepository attractionRepository, EventRepository eventRepository, AttractionMapper attractionMapper) {
        this.attractionRepository = attractionRepository;
        this.eventRepository = eventRepository;
        this.attractionMapper = attractionMapper;
    }

    private boolean isNameUnique(String name) {
        return !attractionRepository.existsByName(name);
    }

    public boolean hasAssociatedEvents(Long id) {
        return !eventRepository.findByAttractionId(id).isEmpty();
    }

    public AttractionResponseDTO createAttraction(AttractionRequestDTO dto) {
        if (!isNameUnique(dto.getName())) {
            throw new BadRequestException("Error creating attraction: An attraction with the name '" + dto.getName() + "' already exists in the system.");
        }
        var attraction = attractionMapper.toEntity(dto);
        attraction = attractionRepository.save(attraction);
        return attractionMapper.toResponseDTO(attraction);

    }

    public List<AttractionResponseDTO> listAttractions() {
        var attractions = attractionRepository.findAll();
        return attractions.stream()
                .map(attractionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AttractionResponseDTO> findByType(AttractionType type) {
        var attractions = attractionRepository.findByType(type);
        if (attractions.isEmpty()) {
            throw new NoContentException("No attractions of type '" + type + "' were found in the system.");
        }
        return attractions.stream()
                .map(attractionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAttraction(Long id) {
        var attraction = attractionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Error retrieving attraction: Attraction with ID " + id + " was not found."));

        if (hasAssociatedEvents(id)) {
            throw new BadRequestException("Error deleting attraction: Attraction with ID " + id + " cannot be deleted because it is associated with one or more events.");
        }
        attractionRepository.delete(attraction);
    }
}
