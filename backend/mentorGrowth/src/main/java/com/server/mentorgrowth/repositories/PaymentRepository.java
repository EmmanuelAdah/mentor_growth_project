package com.server.mentorgrowth.repositories;

import com.server.mentorgrowth.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByReference(String reference);
    Optional<Payment> findById(String id);
    List<Payment> findByMentee_Id(String menteeId);
}
