package com.example.hotel.service;

import com.example.hotel.dto.RequestBookRoomDTO;
import com.example.hotel.dto.SearchRoomDTO;
import com.example.hotel.model.RoomModel;

import java.util.List;

public interface ClientService {
    List<RoomModel> search(SearchRoomDTO dto);

    RequestBookRoomDTO orderRoom(RequestBookRoomDTO dto);
}
