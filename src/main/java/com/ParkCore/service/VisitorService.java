package com.ParkCore.service;

import com.ParkCore.exceptions.BadRequestException;
import com.ParkCore.model.Visitor;
import com.ParkCore.repository.VisitorRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import static com.ParkCore.validation.VisitorValidator.validateVisitorCpf;
import static com.ParkCore.validation.VisitorValidator.validateVisitorPhone;

@Service
public class VisitorService {

    private final VisitorRepository visitorRepository;

    public VisitorService(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    public Visitor registerVisitor(Visitor visitor) {
        validateVisitorCpf(visitor);
        validateVisitorPhone(visitor);
        return visitorRepository.save(visitor);
    }

    public void deleteVisitor(Long visitorId) {
        var visitor = visitorRepository.findById(visitorId)
                .orElseThrow(() -> new ObjectNotFoundException("Visitor not found", Visitor.class));

        checkDeletion(visitorId);

        visitorRepository.delete(visitor);
    }

    private void checkDeletion(Long visitorId) {
        if (visitorRepository.hasTickets(visitorId)) {
            throw new BadRequestException("Cannot remove visitor with purchased tickets");
        }

        if (visitorRepository.hasPendingFeedback(visitorId)) {
            throw new BadRequestException("Cannot remove visitor with pending feedback");
        }
    }
}
