package com.server.mentorgrowth.repositories;

import com.server.mentorgrowth.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByReference(String reference);
}
