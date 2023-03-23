package com.example.hotel.repository;

import com.example.hotel.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
    @Query("select u from UserModel u where u.userName = :username")
    Optional<UserModel> findByUserName(String username);
}
