package com.ParkCore.controller;

import com.ParkCore.model.Visitor;
import com.ParkCore.service.VisitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/visitors")
public class VisitorController {

    private final VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @Operation(summary = "Create a visitor", description = "This functionality registers a new visitor in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Visitor registered successfully!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Visitor.class))),
            @ApiResponse(responseCode = "400", description = "Problem registering the visitor!")
    })
    @PostMapping("/register")
    public ResponseEntity<Visitor> registerVisitor(@RequestBody Visitor visitorRequest) {
        var visitor = visitorService.registerVisitor(visitorRequest);
        return ResponseEntity.status(CREATED).body(visitor);
    }

    @Operation(summary = "Delete a visitor", description = "This functionality deletes a visitor from the system by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Visitor deleted successfully!"),
            @ApiResponse(responseCode = "404", description = "Visitor not found"),
            @ApiResponse(responseCode = "400", description = "Could not delete the visitor"),
            @ApiResponse(responseCode = "500", description = "Problem with the API!")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisitor(@PathVariable Long id) {
        visitorService.deleteVisitor(id);
        return ResponseEntity.noContent().build();
    }
}
