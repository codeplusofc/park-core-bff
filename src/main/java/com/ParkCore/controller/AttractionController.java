package com.ParkCore.controller;

import com.ParkCore.enums.AttractionType;
import com.ParkCore.model.Attraction;
import com.ParkCore.service.AttractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequestMapping("/attractions")
public class AttractionController {

    private final AttractionService attractionService;

    public AttractionController(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    @Operation(summary = "Search for attractions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found attractions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Attraction.class))),
    })
    @GetMapping
    public ResponseEntity<List<Attraction>> listAttractions() {
        var attractions = attractionService.listAttractions();
        return ResponseEntity.ok(attractions);
    }

    @Operation(summary = "Search by attraction type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found attractions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Attraction.class))),
            @ApiResponse(responseCode = "404", description = "No attractions found for the specified type")
    })
    @GetMapping("/attractionType/{type}")
    public ResponseEntity<List<Attraction>> getAttractionsByType(@PathVariable AttractionType type) {
        var attractions = attractionService.findByType(type);
        return ResponseEntity.ok(attractions);
    }

    @Operation(summary = "Create attractions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Attraction created successfully!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Attraction.class))),
            @ApiResponse(responseCode = "400", description = "The attraction has already been registered")
    })
    @PostMapping
    public ResponseEntity<Attraction> createAttraction(@RequestBody Attraction attractionRequest) {
        var attraction = attractionService.createAttraction(attractionRequest);
        return ResponseEntity.status(CREATED).body(attraction);
    }

    @Operation(summary = "Delete Attraction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Attraction deleted"),
            @ApiResponse(responseCode = "404", description = "Attraction not found"),
            @ApiResponse(responseCode = "400", description = "Attraction not deleted")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttraction(@PathVariable Long id) {
        attractionService.deleteAttraction(id);
        return ResponseEntity.noContent().build();
    }
}