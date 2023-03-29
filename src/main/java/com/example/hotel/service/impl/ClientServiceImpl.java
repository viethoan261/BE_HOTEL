package com.example.hotel.service.impl;

import com.example.hotel.dto.RequestBookRoomDTO;
import com.example.hotel.dto.SearchRoomDTO;
import com.example.hotel.model.BookedRoomModel;
import com.example.hotel.model.BookingModel;
import com.example.hotel.model.ClientModel;
import com.example.hotel.model.RoomModel;
import com.example.hotel.repository.BookedRoomRepository;
import com.example.hotel.repository.BookingRepository;
import com.example.hotel.repository.ClientRepository;
import com.example.hotel.repository.RoomRepository;
import com.example.hotel.repository.UserRepository;
import com.example.hotel.service.ClientService;
import com.example.hotel.utils.enumm.BookingStatus;
import com.example.hotel.utils.enumm.RoomBookedStatus;
import com.example.hotel.utils.enumm.RoomStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private RoomRepository repository;

    @Autowired
    private BookedRoomRepository bookedRoomRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public List<RoomModel> search(SearchRoomDTO dto) {
        List<RoomModel> rooms = repository.findAll().stream().filter(t -> t.getStatus().equals(RoomStatus.FREE))
                .collect(Collectors.toList());
        List<UUID> bookedRoomModelsExpire = bookedRoomRepository
                .bookedRoomExpire(dto.getCheckIn(), dto.getCheckOut()).stream().map(t -> t.getRoomID())
                .collect(Collectors.toList());
        List<RoomModel> roomExpire = repository.findRoomByIds(bookedRoomModelsExpire);
        rooms.addAll(roomExpire);
        return rooms;
    }

    @Transactional
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
            clientModel.setBankName(dto.getBankName());
            clientModel.setBankNumber(dto.getBankNumber());
            clientModel.setFullName(dto.getFullName());
            clientModel.setTel(dto.getTel());
            clientModel.setIsConfirmed(Boolean.FALSE);
            clientModel.setIdCard(dto.getIdCard());
            clientModel = clientRepository.save(clientModel);

            //save booking
            BookingModel bookingModel = new BookingModel();
            bookingModel.setClientID(clientModel.getId());
            bookingModel.setNote(dto.getNote());
            bookingModel.setBookingDate(LocalDateTime.now());
//            bookingModel.setUserID(UUID.fromString(this.getIdUserCurrent()));
            bookingModel.setStatus(BookingStatus.PENDING);
            bookingModel = bookingRepository.save(bookingModel);

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
                bookedRoom.setBookingId(bookingModel.getId());
                bookedRoom.setStatus(RoomBookedStatus.PENDING);
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
            if (RoomStatus.FREE.equals(room.getStatus())) {
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
            return false;
        }
    }
}
