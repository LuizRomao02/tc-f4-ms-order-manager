package com.java.fiap.ordermanager.domain.repository;

import com.java.fiap.ordermanager.domain.entity.Payment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {}
