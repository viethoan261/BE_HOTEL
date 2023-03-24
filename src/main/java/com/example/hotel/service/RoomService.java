package com.example.hotel.service;

import com.example.hotel.dto.CreateRoomDTO;
import com.example.hotel.dto.RequestBookRoomDTO;
import com.example.hotel.dto.SearchRoomDTO;
import com.example.hotel.dto.TestDTO;
import com.example.hotel.model.RoomModel;

import java.util.List;
import java.util.UUID;

public interface RoomService {
    CreateRoomDTO create(CreateRoomDTO dto);

    RoomModel update(UUID id, CreateRoomDTO dto);

    TestDTO test(UUID id, TestDTO dto);
}
