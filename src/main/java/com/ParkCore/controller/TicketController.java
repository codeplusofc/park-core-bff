package com.ParkCore.controller;

import com.ParkCore.model.Ticket;
import com.ParkCore.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(summary = "Issue ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket issued successfully!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ticket.class))),
            @ApiResponse(responseCode = "404", description = "Visitor not found"),
            @ApiResponse(responseCode = "404", description = "Attraction not found"),
    })
    @PostMapping
    public ResponseEntity<Ticket> issueTicket(@RequestBody Ticket ticketRequest) {
        var ticket = ticketService.issueTicket(ticketRequest);
        return ResponseEntity.ok(ticket);
    }

	@Operation(summary = "Cancel ticket")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Ticket deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Ticket not found"),
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<String> cancelTicket(@PathVariable Long id) {
        ticketService.cancelTicket(id);
		return  ResponseEntity.ok("Ticket canceled successfully!");
	}

    @Operation(summary = "Get tickets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tickets found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ticket.class))),
    })
    @GetMapping
    public ResponseEntity<List<Ticket>> listTickets() {
        var tickets = ticketService.listTickets();
        return ResponseEntity.ok(tickets);
    }
}
