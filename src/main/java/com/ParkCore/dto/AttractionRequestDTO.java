package com.ParkCore.dto;

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

    @NotBlank(message = "O nome da atração é obrigatório.")
    private String name;

    @NotBlank(message = "...")
    private String description ;

    @NotNull(message = "...")
    private AttractionType type;

    @Min(value =1, message = "...")
    @Max(value = 1000, message = "...")
    private int maximumCapacity;



}
