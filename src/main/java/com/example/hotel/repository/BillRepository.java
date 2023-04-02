package com.example.hotel.repository;

import com.example.hotel.model.BillModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<BillModel, UUID> {
}
