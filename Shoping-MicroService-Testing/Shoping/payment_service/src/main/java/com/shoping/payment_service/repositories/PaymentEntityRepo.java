package com.shoping.payment_service.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoping.payment_service.entities.PaymentEntity;

@Repository
public interface PaymentEntityRepo extends JpaRepository<PaymentEntity, Long> {

    Optional<List<PaymentEntity>> findAllByUserId(long userId);

}
