package com.ParkCore.service;

import com.ParkCore.enums.AttractionType;
import com.ParkCore.exceptions.BadRequestException;
import com.ParkCore.exceptions.NoContentException;
import com.ParkCore.model.Attraction;
import com.ParkCore.repository.AttractionRepository;
import com.ParkCore.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttractionService {

    private final AttractionRepository attractionRepository;
    private final EventRepository eventRepository;

    public AttractionService(AttractionRepository attractionRepository, EventRepository eventRepository) {
        this.attractionRepository = attractionRepository;
        this.eventRepository = eventRepository;
    }

    private boolean isNameUnique(String name) {
        return !attractionRepository.existsByName(name);
    }

    public boolean hasAssociatedEvents(Long id) {
        return !eventRepository.findByAttractionId(id).isEmpty();
    }

    public Attraction createAttraction(Attraction attraction) {
        if (!isNameUnique(attraction.getName())) {
            throw new BadRequestException("Error creating attraction: An attraction with the name '" + attraction.getName() + "' already exists in the system.");
        }

        return attractionRepository.save(attraction);
    }

    public List<Attraction> listAttractions() {
        return attractionRepository.findAll();
    }

    public List<Attraction> findByType(AttractionType type) {
        var attractions = attractionRepository.findByType(type);
        if (attractions.isEmpty()) {
            throw new NoContentException("No attractions of type '" + type + "' were found in the system.");
        }
        return attractions;
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
