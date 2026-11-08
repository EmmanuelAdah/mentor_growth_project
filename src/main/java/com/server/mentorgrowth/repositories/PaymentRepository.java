package com.server.mentorgrowth.repositories;

import com.server.mentorgrowth.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByReference(String reference);

    @Query("SELECT p FROM Payment p " +
            "WHERE p.id = :paymentId " +
            "AND (p.mentee.id = :userId OR p.mentor.id = :userId)")
    Optional<Payment> findByIdAndParticipantId(@Param("paymentId") String paymentId,
                                               @Param("userId") String userId);

    @Query("SELECT p FROM Payment p " +
            "WHERE p.reference = :reference " +
            "AND (p.mentee.id = :userId OR p.mentor.id = :userId)")
    Optional<Payment> findByReferenceAndParticipantId(@Param("reference") String reference,
                                                       @Param("userId") String userId);

    @Query("SELECT p FROM Payment p " +
            "WHERE p.mentee.id = :userId OR p.mentor.id = :userId")
    List<Payment> findByParticipantId(@Param("userId") String userId);
}
