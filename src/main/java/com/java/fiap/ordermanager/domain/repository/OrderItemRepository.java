package com.java.fiap.ordermanager.domain.repository;

import com.java.fiap.ordermanager.domain.entity.OrderItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {}
