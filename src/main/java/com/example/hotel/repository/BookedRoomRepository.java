package com.example.hotel.repository;

import com.example.hotel.model.BookedRoomModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookedRoomRepository extends JpaRepository<BookedRoomModel, UUID> {

    @Query("select br from BookedRoomModel br where br.checkIn > :end or br.checkOut < :start")
    List<BookedRoomModel> bookedRoomExpire(LocalDateTime start, LocalDateTime end);

    @Query("select br from BookedRoomModel br where br.bookingId = :bookingId")
    List<BookedRoomModel> findByBookingId(UUID bookingId);
}
