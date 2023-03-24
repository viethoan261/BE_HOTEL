package com.example.hotel.service.impl;

import com.example.hotel.dto.CreateRoomDTO;
import com.example.hotel.dto.RequestBookRoomDTO;
import com.example.hotel.dto.SearchRoomDTO;
import com.example.hotel.dto.TestDTO;
import com.example.hotel.mapper.RoomMapper;
import com.example.hotel.model.BookedRoomModel;
import com.example.hotel.model.ClientModel;
import com.example.hotel.model.RoomModel;
import com.example.hotel.repository.BookedRoomRepository;
import com.example.hotel.repository.ClientRepository;
import com.example.hotel.repository.RoomRepository;
import com.example.hotel.service.RoomService;
import com.example.hotel.utils.enumm.RoomStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository repository;

    @Autowired
    private BookedRoomRepository bookedRoomRepository;

    @Override
    public CreateRoomDTO create(CreateRoomDTO dto) {
        RoomModel room = RoomMapper.INSTANCE.from(dto);
        room.setIsBooked(Boolean.FALSE);
        room.setStatus(RoomStatus.FREE);
        repository.save(room);
        return dto;
    }

    @Override
    public RoomModel update(UUID id, CreateRoomDTO dto) {
        Optional<RoomModel> roomOpt = repository.findById(id);

        if (roomOpt.isEmpty()) {
            return null;
        }

        RoomModel room = roomOpt.get();

        room = RoomMapper.INSTANCE.from(dto);
        repository.save(room);

        return repository.save(room);
    }

    @Override
    public TestDTO test(UUID id, TestDTO dto) {
        BookedRoomModel bookedRoomModel = new BookedRoomModel();
        bookedRoomModel.setCheckIn(dto.getCheckIn());
        bookedRoomModel.setCheckOut(dto.getCheckOut());
        bookedRoomModel.setPrice(dto.getPrice());
        bookedRoomModel.setNote(dto.getNote());
        bookedRoomModel.setRoomID(id);

        bookedRoomRepository.save(bookedRoomModel);

        return dto;
    }
}
