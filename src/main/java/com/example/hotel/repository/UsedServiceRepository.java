package com.example.hotel.repository;

import com.example.hotel.model.UsedServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UsedServiceRepository extends JpaRepository<UsedServiceModel, UUID> {
    @Query("select usm from UsedServiceModel usm where usm.bookingID = :bookingID and usm.bookiedRoomID = :roomID")
    List<UsedServiceModel> findByBookingIDAndRoomID(UUID bookingID, UUID roomID);
}
