package com.server.mentorgrowth.repositories;

import com.server.mentorgrowth.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByReference(String reference);
    Optional<Payment> findById(String id);
    List<Payment> findByMentee_Id(String menteeId);

    @Query("SELECT p FROM Payment p " +
            "WHERE p.id = :paymentId " +
            "AND (p.mentee.id = :userId OR p.mentor.id = :userId)")
    Optional<Payment> findByIdAndParticipantId(@Param("paymentId") String paymentId,
                                               @Param("userId") String userId);
}
