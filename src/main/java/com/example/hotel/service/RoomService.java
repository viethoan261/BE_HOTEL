package com.example.hotel.service;

import com.example.hotel.dto.CreateRoomDTO;
import com.example.hotel.model.RoomModel;

import java.util.UUID;

public interface RoomService {
    CreateRoomDTO create(CreateRoomDTO dto);

    RoomModel update(UUID id, CreateRoomDTO dto);
}
