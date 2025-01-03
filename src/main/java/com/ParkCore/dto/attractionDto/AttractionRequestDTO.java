package com.ParkCore.dto.attractionDto;

import com.ParkCore.enums.AttractionType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttractionRequestDTO {


    private String name;


    private String description ;


    private AttractionType type;


    private int maximumCapacity;


    public AttractionRequestDTO(String name, String description, AttractionType type, int maximumCapacity) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.maximumCapacity = maximumCapacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AttractionType getType() {
        return type;
    }

    public void setType(AttractionType type) {
        this.type = type;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }
}
