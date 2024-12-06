package com.ParkCore.service;

import com.ParkCore.exceptions.BadRequestException;
import com.ParkCore.model.Attraction;
import com.ParkCore.model.Ticket;
import com.ParkCore.model.Visitor;
import com.ParkCore.repository.AttractionRepository;
import com.ParkCore.repository.TicketRepository;
import com.ParkCore.repository.VisitorRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TicketService {

	private final TicketRepository ticketRepository;
	private final VisitorRepository visitorRepository;
	private final AttractionRepository attractionRepository;

	public TicketService(TicketRepository ticketRepository, VisitorRepository visitorRepository, AttractionRepository attractionRepository) {
		this.ticketRepository = ticketRepository;
		this.visitorRepository = visitorRepository;
		this.attractionRepository = attractionRepository;
	}

	public Ticket issueTicket(Ticket ticketRequest) {
		var visitor = visitorRepository.findById(ticketRequest.getVisitor().getId())
				.orElseThrow(() -> new ObjectNotFoundException("Visitor not found", Visitor.class));

		var attraction = attractionRepository.findById(ticketRequest.getAttraction().getId())
				.orElseThrow(() -> new ObjectNotFoundException("Attraction not found", Attraction.class));

		var ticket = new Ticket();

		ticket.setVisitor(visitor);
		ticket.setAttraction(attraction);
		ticket.setIssueDate(new Date());
		ticket.setType(ticketRequest.getType());
		ticket.setStatus(ticketRequest.getStatus());
		ticket.setCode(UUID.randomUUID().toString());
		ticket.setVisitDate(ticketRequest.getVisitDate());

		return ticketRepository.save(ticket);
	}

	public void cancelTicket(Long id) {
		var ticket = ticketRepository.findById(id)
				.orElseThrow(() -> new ObjectNotFoundException("Ticket not found",Ticket.class));

		if (!ticket.getStatus().equals("active")) {
			throw new BadRequestException("Ticket cannot be canceled");
		}
		ticket.setStatus("canceled");
		ticketRepository.save(ticket);
	}

	public List<Ticket> listTickets() {
		return ticketRepository.findAll();
	}
}
