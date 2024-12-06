package com.ParkCore.service;

import com.ParkCore.exceptions.BadRequestException;
import com.ParkCore.model.Visitor;
import com.ParkCore.repository.VisitorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class VisitorServiceTest {

    @Mock
    private VisitorRepository visitorRepository;

    @InjectMocks
    private VisitorService visitorService;

    @Test
    public void shouldRegisterVisitor() {

        var visitor = mock(Visitor.class);

        given(visitor.getId()).willReturn(1L);
        given(visitor.getEmail()).willReturn("henrique@gmail.com");
        given(visitor.getCpf()).willReturn("83735288065");
        given(visitor.getName()).willReturn("Henrique");
        given(visitor.getPhone()).willReturn("(11) 93865-4255");

        given(visitorRepository.save(visitor)).willReturn(visitor);

        var result = visitorService.registerVisitor(visitor);

        assertEquals(1L, result.getId());
        assertEquals("henrique@gmail.com", result.getEmail());
        assertEquals("83735288065", result.getCpf());
        assertEquals("Henrique", result.getName());
        assertEquals("(11) 93865-4255", result.getPhone());

    }

    @Test
    public void shouldDeleteVisitor() {

        var visitor = mock(Visitor.class);

        given(visitor.getId()).willReturn(1L);

        given(visitorRepository.findById(visitor.getId())).willReturn(Optional.of(visitor));

        visitorService.deleteVisitor(visitor.getId());
    }

    @Test
    void shouldThrowExceptionWhenVisitorNotFound() {
        thenThrownBy(() -> visitorService.deleteVisitor(null)).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenVisitorHasPurchasedTickets() {

        var visitor = mock(Visitor.class);

        given(visitorRepository.findById(visitor.getId())).willReturn(Optional.of(visitor));
        given(visitorRepository.hasTickets(visitor.getId())).willReturn(true);

        thenThrownBy(() -> visitorService.deleteVisitor(visitor.getId())).isInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldThrowExceptionWhenVisitorHasPendingFeedbacks() {

        var visitor = mock(Visitor.class);

        given(visitorRepository.findById(visitor.getId())).willReturn(Optional.of(visitor));
        given(visitorRepository.hasPendingFeedback(visitor.getId())).willReturn(true);

        thenThrownBy(() -> visitorService.deleteVisitor(visitor.getId())).isInstanceOf(BadRequestException.class);
    }
}
