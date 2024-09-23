package com.shoping.user_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shoping.user_service.entities.Users;

public interface UsersRepo extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByPhone(String phone);

}
