package com.example.hotel.service.impl;

import com.example.hotel.dto.RequestBookRoomDTO;
import com.example.hotel.model.BookedRoomModel;
import com.example.hotel.model.ClientModel;
import com.example.hotel.model.RoomModel;
import com.example.hotel.repository.BookedRoomRepository;
import com.example.hotel.repository.ClientRepository;
import com.example.hotel.repository.RoomRepository;
import com.example.hotel.service.RoomBookedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomBookedServiceImpl implements RoomBookedService {
    @Autowired
    private BookedRoomRepository bookedRoomRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    @Override
    public RequestBookRoomDTO create(RequestBookRoomDTO dto) {
        List<String> ids = dto.getIdsRoom();
        if (this.checkAvailableRoom(ids, dto)) {
            List<RoomModel> rooms = roomRepository.findRoomByIds(ids);
            ClientModel clientModel = new ClientModel();

            //save client
            clientModel.setAddress(dto.getAddress());
            clientModel.setEmail(dto.getEmail());
//            clientModel.setNote(dto.getNote());
            clientModel.setFullName(dto.getFullName());
            clientModel.setTel(dto.getTel());
            clientModel.setIsConfirmed(Boolean.FALSE);
            clientRepository.save(clientModel);

            for(int i = 0; i < ids.size(); i++) {
                //change status room
                RoomModel room = rooms.get(i);
                room.setIsBooked(Boolean.TRUE);
                roomRepository.save(room);

                //save bookedRoom
                BookedRoomModel bookedRoom = new BookedRoomModel();
                bookedRoom.setRoomID(room.getId());
                bookedRoom.setNote(dto.getNote());
                bookedRoom.setPrice(room.getPrice());
                bookedRoom.set
            }

        } else {
            return null;
        }
    }

    private Boolean checkAvailableRoom(List<String> ids, RequestBookRoomDTO dto) {
        List<String> allIds = new ArrayList<>();
        List<RoomModel> rooms = roomRepository.findAll();
        List<String> bookedRoomModelsExpire = bookedRoomRepository
                .bookedRoomExpire(dto.getCheckIn(), dto.getCheckOut()).stream().map(t -> t.getRoomID().toString())
                .collect(Collectors.toList());
        allIds.addAll(bookedRoomModelsExpire);

        for (RoomModel room: rooms) {
            if (Boolean.FALSE.equals(room.getIsBooked())) {
                allIds.add(room.getId().toString());
            }
        }

        if (Arrays.deepEquals(allIds.toArray(), ids.toArray())) {
            return true;
        } else {
            return false;
        }
    }
}
