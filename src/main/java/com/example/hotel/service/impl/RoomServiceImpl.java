package com.example.hotel.service.impl;

import com.example.hotel.dto.CreateRoomDTO;
import com.example.hotel.dto.SearchRoomDTO;
import com.example.hotel.dto.TestDTO;
import com.example.hotel.mapper.RoomMapper;
import com.example.hotel.model.BookedRoomModel;
import com.example.hotel.model.RoomModel;
import com.example.hotel.repository.BookedRoomRepository;
import com.example.hotel.repository.RoomRepository;
import com.example.hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    @Override
    public List<RoomModel> search(SearchRoomDTO dto) {
        List<RoomModel> rooms = repository.findAll().stream().filter(t -> t.getIsBooked().equals(Boolean.FALSE))
                .collect(Collectors.toList());
        List<UUID> bookedRoomModelsExpire = bookedRoomRepository
                .bookedRoomExpire(dto.getCheckIn(), dto.getCheckOut()).stream().map(t -> t.getRoomID())
                .collect(Collectors.toList());
        List<RoomModel> roomExpire = repository.findRoomByIds(bookedRoomModelsExpire);
        rooms.addAll(roomExpire);
        return rooms;
    }
}
