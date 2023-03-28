package com.example.hotel.repository;

import com.example.hotel.model.BookingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<BookingModel, UUID> {
    @Query("Select b from BookingModel b where b.status <> 'CANCEL' AND b.status <> 'DONE' order by b.status desc")
    List<BookingModel> getBookings();
}
