package com.shoping.user_service.services;

import java.util.List;
import java.util.Optional;

import com.shoping.user_service.entities.Users;
import com.shoping.user_service.models.UserOrderResponse;

public interface UsersService {
    Users addUser(Users user);

    List<Users> getAllUsers();

    Optional<Users> getUserByUserId(long userId);

    Users deleteUserByUserId(long userId);

    Users updateUser(Users user, long userId);

    Users addBalance(long userId, double balance);

    public Users doPayment(long userId, double balance);

    Users undoPayment(long userId, double balance);

    double getBalance(long userId);

    UserOrderResponse getAllOrdersOfAUser(long userId);

    UserOrderResponse getAllPaymentsOfAUser(long userId);
}
