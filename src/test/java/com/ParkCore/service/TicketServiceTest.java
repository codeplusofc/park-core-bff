package com.ParkCore.service;

import com.ParkCore.exceptions.BadRequestException;
import com.ParkCore.model.Attraction;
import com.ParkCore.model.Ticket;
import com.ParkCore.model.Visitor;
import com.ParkCore.repository.AttractionRepository;
import com.ParkCore.repository.TicketRepository;
import com.ParkCore.repository.VisitorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private VisitorRepository visitorRepository;
    @Mock
    private AttractionRepository attractionRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    public void mustIssueTicket() {
        var visitor = new Visitor();
        visitor.setId(1L);

        var attraction = new Attraction();
        attraction.setId(1L);

        var ticketRequest = new Ticket();
        ticketRequest.setVisitor(visitor);
        ticketRequest.setAttraction(attraction);
        ticketRequest.setType("VIP");
        ticketRequest.setStatus("active");
        ticketRequest.setVisitDate(new Date());

        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        when(attractionRepository.findById(1L)).thenReturn(Optional.of(attraction));

        var savedTicket = new Ticket();
        savedTicket.setCode("generated-code");
        savedTicket.setVisitor(visitor);
        savedTicket.setAttraction(attraction);
        savedTicket.setIssueDate(new Date());
        savedTicket.setType("VIP");
        savedTicket.setStatus("active");
        savedTicket.setVisitDate(ticketRequest.getVisitDate());

        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);

        var result = ticketService.issueTicket(ticketRequest);

        assertNotNull(result);
        assertEquals("generated-code", result.getCode());
        assertEquals(visitor, result.getVisitor());
        assertEquals(attraction, result.getAttraction());
        assertEquals("VIP", result.getType());
        assertEquals("active", result.getStatus());
        assertEquals(ticketRequest.getVisitDate(), result.getVisitDate());

        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    public void shouldCancelTicket(){
        var ticket = mock(Ticket.class);

        given(ticket.getId()).willReturn(1L);
        given(ticket.getStatus()).willReturn("active");
        given(ticketRepository.findById(ticket.getId())).willReturn(Optional.of(ticket));

        ticketService.cancelTicket(1L);

        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    public void shouldListTickets(){
        var ticketOne = mock(Ticket.class);
        var ticketTwo = mock(Ticket.class);

        given(ticketOne.getId()).willReturn(1L);
        given(ticketTwo.getId()).willReturn(2L);
        given(ticketRepository.findAll()).willReturn(List.of(ticketOne, ticketTwo));

        var result = ticketService.listTickets();

        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    public void shouldThrowExceptionWhenCancelTicket(){
        var ticket = mock(Ticket.class);

        given(ticket.getId()).willReturn(1L);
        given(ticket.getStatus()).willReturn("canceled");
        given(ticketRepository.findById(ticket.getId())).willReturn(Optional.of(ticket));

        thenThrownBy(() -> ticketService.cancelTicket(ticket.getId())).isInstanceOf(BadRequestException.class);
    }
}
