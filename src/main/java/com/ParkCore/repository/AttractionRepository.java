package com.ParkCore.repository;

import com.ParkCore.enums.AttractionType;
import com.ParkCore.model.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    boolean existsByName(String name);

    List<Attraction> findByType(AttractionType type);
}
