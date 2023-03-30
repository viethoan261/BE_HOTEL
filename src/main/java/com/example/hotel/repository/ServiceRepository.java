package com.example.hotel.repository;

import com.example.hotel.model.ServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceModel, UUID> {
    @Query("select s from ServiceModel s where s.status = 'ACTIVE'")
    List<ServiceModel> getAllService();
}
