package com.ParkCore.repository;

import com.ParkCore.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    @Query("SELECT COUNT(i) > 0 FROM Ticket i WHERE i.visitor.id = :visitorId")
    boolean hasTickets(@Param("visitorId") Long visitorId);

    @Query("SELECT COUNT(f) > 0 FROM Feedback f WHERE f.visitor.id = :visitorId")
    boolean hasPendingFeedback(@Param("visitorId") Long visitorId);

    boolean existsByCpf(String ssn);
}
