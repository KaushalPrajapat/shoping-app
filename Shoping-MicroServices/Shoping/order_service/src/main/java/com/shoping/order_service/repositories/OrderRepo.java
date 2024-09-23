package com.shoping.order_service.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoping.order_service.entities.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderIdAndOrderStatusNot(long orderId, String string);

    Optional<Order> findByOrderIdAndOrderStatus(long orderId, String string);

    Optional<Order> findByOrderId(long orderId);

    List<Optional<Order>> findAllByUserId(long userId);

}
