package com.example.hotel.service.impl;

import com.example.hotel.dto.CreateRoomDTO;
import com.example.hotel.mapper.RoomMapper;
import com.example.hotel.model.RoomModel;
import com.example.hotel.repository.RoomRepository;
import com.example.hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository repository;

    @Override
    public CreateRoomDTO create(CreateRoomDTO dto) {
        RoomModel room = RoomMapper.INSTANCE.from(dto);
        room.setIsBooked(Boolean.FALSE);
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
}
