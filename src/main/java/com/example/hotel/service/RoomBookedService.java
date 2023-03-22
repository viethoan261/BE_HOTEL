package com.example.hotel.service;

import com.example.hotel.dto.RequestBookRoomDTO;
import com.example.hotel.model.BookedRoomModel;

public interface RoomBookedService {
    RequestBookRoomDTO create (RequestBookRoomDTO dto);
}
