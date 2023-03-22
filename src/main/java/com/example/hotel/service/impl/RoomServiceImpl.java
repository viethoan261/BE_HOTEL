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

    @Autowired
    private ClientRepository clientRepository;

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

    @Override
    public RequestBookRoomDTO orderRoom(RequestBookRoomDTO dto) {
        List<UUID> ids = dto.getIdsRoom().stream().map(t -> UUID.fromString(t)).collect(Collectors.toList());
        if (this.checkAvailableRoom(ids, dto)) {
            List<RoomModel> rooms = repository.findRoomByIds(ids);
            ClientModel clientModel = new ClientModel();

            //save client
            clientModel.setAddress(dto.getAddress());
            clientModel.setEmail(dto.getEmail());
//            clientModel.setNote(dto.getNote());
            clientModel.setFullName(dto.getFullName());
            clientModel.setTel(dto.getTel());
            clientModel.setIsConfirmed(Boolean.FALSE);
            clientModel.setIdCard(dto.getIdCard());
            clientRepository.save(clientModel);

            for (int i = 0; i < ids.size(); i++) {
                //change status room
                RoomModel room = rooms.get(i);
                room.setIsBooked(Boolean.TRUE);
                room.setStatus(RoomStatus.PENDING);
                repository.save(room);

                //save bookedRoom
                BookedRoomModel bookedRoom = new BookedRoomModel();
                bookedRoom.setRoomID(room.getId());
                bookedRoom.setNote(dto.getNote());
                bookedRoom.setPrice(room.getPrice());
                bookedRoom.setIsCheckIn(Boolean.FALSE);
                bookedRoom.setCheckIn(dto.getCheckIn());
                bookedRoom.setCheckOut(dto.getCheckOut());
                bookedRoomRepository.save(bookedRoom);
            }
            return dto;
        } else {
            return null;
        }
    }

    private Boolean checkAvailableRoom(List<UUID> ids, RequestBookRoomDTO dto) {
        List<UUID> allIds = new ArrayList<>();
        List<RoomModel> rooms = repository.findAll();
        List<UUID> bookedRoomModelsExpire = bookedRoomRepository
                .bookedRoomExpire(dto.getCheckIn(), dto.getCheckOut()).stream().map(t -> t.getRoomID())
                .collect(Collectors.toList());
        allIds.addAll(bookedRoomModelsExpire);

        for (RoomModel room : rooms) {
            if (Boolean.FALSE.equals(room.getIsBooked())) {
                allIds.add(room.getId());
            }
        }

        int size = ids.size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (allIds.contains(ids.get(i))) {
                count++;
            }
        }
        if (count == size) {
            return true;
        } else {
            return null;
        }
    }
}
