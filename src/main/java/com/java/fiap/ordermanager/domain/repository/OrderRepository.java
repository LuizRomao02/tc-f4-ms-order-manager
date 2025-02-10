package com.java.fiap.ordermanager.domain.repository;

import com.java.fiap.ordermanager.domain.entity.Orders;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, UUID> {}
