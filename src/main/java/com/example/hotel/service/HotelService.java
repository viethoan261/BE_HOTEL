package com.example.hotel.service;

import com.example.hotel.dto.CreateRoomDTO;
import com.example.hotel.dto.TestDTO;
import com.example.hotel.model.RoomModel;

import java.util.UUID;

public interface HotelService {
    CreateRoomDTO create(CreateRoomDTO dto);

    RoomModel update(UUID id, CreateRoomDTO dto);

    TestDTO test(UUID id, TestDTO dto);

    void approve(UUID bookingID, Float selloff);

    void cancel(UUID bookingID);

    void checkin(UUID bookingID);
}
