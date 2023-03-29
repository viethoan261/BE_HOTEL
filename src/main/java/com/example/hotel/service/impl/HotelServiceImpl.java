package com.example.hotel.service.impl;

import com.example.hotel.dto.BookingListDTO;
import com.example.hotel.dto.CreateRoomDTO;
import com.example.hotel.dto.InfoBookingDTO;
import com.example.hotel.dto.TestDTO;
import com.example.hotel.mapper.RoomMapper;
import com.example.hotel.model.BookedRoomModel;
import com.example.hotel.model.BookingModel;
import com.example.hotel.model.ClientModel;
import com.example.hotel.model.RoomModel;
import com.example.hotel.repository.BookedRoomRepository;
import com.example.hotel.repository.BookingRepository;
import com.example.hotel.repository.ClientRepository;
import com.example.hotel.repository.RoomRepository;
import com.example.hotel.repository.UserRepository;
import com.example.hotel.service.HotelService;
import com.example.hotel.utils.enumm.BookingStatus;
import com.example.hotel.utils.enumm.RoomBookedStatus;
import com.example.hotel.utils.enumm.RoomStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookedRoomRepository bookedRoomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public CreateRoomDTO create(CreateRoomDTO dto) {
        RoomModel room = RoomMapper.INSTANCE.from(dto);
        room.setIsBooked(Boolean.FALSE);
        room.setStatus(RoomStatus.FREE);
        roomRepository.save(room);
        return dto;
    }

    @Override
    public RoomModel update(UUID id, CreateRoomDTO dto) {
        Optional<RoomModel> roomOpt = roomRepository.findById(id);

        if (roomOpt.isEmpty()) {
            return null;
        }

        RoomModel room = roomOpt.get();
        room.setName(dto.getName());
        room.setType(dto.getType());
        room.setPrice(dto.getPrice());
        room.setDescription(dto.getDescription());
        room.setImage(dto.getImage());

        roomRepository.save(room);

        return roomRepository.save(room);
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

    @Transactional
    @Override
    public void approve(UUID bookingID, Float selloff) {
        Optional<BookingModel> bookingModel = bookingRepository.findById(bookingID);
        if (bookingModel.isEmpty()) {
            return;
        }
        //update booking
        BookingModel booking = bookingModel.get();
        if (!booking.getStatus().equals(BookingStatus.PENDING)) {
            return;
        }
        booking.setStatus(BookingStatus.ACCEPT);
        booking.setUserID(UUID.fromString(this.getIdUserCurrent()));
        booking.setSelloff(selloff);
        bookingRepository.save(booking);

        //update booked room
        List<BookedRoomModel> bookedRoomModels = bookedRoomRepository.findByBookingId(bookingID);
        for (BookedRoomModel bookedRoom: bookedRoomModels
             ) {
            bookedRoom.setSelloff(selloff);
        }
        bookedRoomRepository.saveAll(bookedRoomModels);

        //update client
        ClientModel client = clientRepository.findById(booking.getClientID()).get();
        client.setIsConfirmed(Boolean.TRUE);
        clientRepository.save(client);
    }

    @Transactional
    @Override
    public void cancel(UUID bookingID) {
        Optional<BookingModel> bookingModel = bookingRepository.findById(bookingID);
        if (bookingModel.isEmpty()) {
            return;
        }
        //update booking
        BookingModel booking = bookingModel.get();
        if (booking.getStatus().equals(BookingStatus.DONE) || booking.getStatus().equals(BookingStatus.CANCEL)) {
            return;
        }
        booking.setStatus(BookingStatus.CANCEL);
        if (booking.getUserID() == null) {
            booking.setUserID(UUID.fromString(this.getIdUserCurrent()));
        }
        bookingRepository.save(booking);

        //update booked room
        List<BookedRoomModel> bookedRoomModels = bookedRoomRepository.findByBookingId(bookingID);
        for (BookedRoomModel bookedRoom: bookedRoomModels
        ) {
            bookedRoom.setStatus(RoomBookedStatus.CANCEL);
        }
        bookedRoomRepository.saveAll(bookedRoomModels);

        List<RoomModel> rooms = roomRepository.findRoomByIds(bookedRoomModels.stream()
                .map(t -> t.getRoomID()).collect(Collectors.toList()));
        for (RoomModel room: rooms
        ) {
            room.setStatus(RoomStatus.FREE);
            room.setIsBooked(Boolean.FALSE);
        }
        roomRepository.saveAll(rooms);
    }

    @Transactional
    @Override
    public void checkin(UUID bookingID) {
        Optional<BookingModel> bookingModel = bookingRepository.findById(bookingID);
        if (bookingModel.isEmpty()) {
            return;
        }
        BookingModel booking = bookingModel.get();
        if (!booking.getStatus().equals(BookingStatus.ACCEPT)) {
            return;
        }

        //update booked
        List<BookedRoomModel> bookedRoomModels = bookedRoomRepository.findByBookingId(bookingID);
        for (BookedRoomModel bookedRoom: bookedRoomModels
        ) {
            bookedRoom.setIsCheckIn(Boolean.TRUE);
            bookedRoom.setStatus(RoomBookedStatus.PROGRESS);
        }
        bookedRoomRepository.saveAll(bookedRoomModels);

        //update room
        List<RoomModel> rooms = roomRepository.findRoomByIds(bookedRoomModels.stream()
                .map(t -> t.getRoomID()).collect(Collectors.toList()));
        for (RoomModel room: rooms
        ) {
            room.setStatus(RoomStatus.PROGRESS);
        }
        roomRepository.saveAll(rooms);
    }

    @Override
    public List<BookingListDTO> getBooking() {
        List<BookingListDTO> dtos = new ArrayList<>();
        List<BookingModel> bookingModels = bookingRepository.getBookings();
        for (BookingModel booking: bookingModels) {
            List<InfoBookingDTO> infos = new ArrayList<>();
            BookingListDTO dto = new BookingListDTO();
            ClientModel client = clientRepository.findById(booking.getClientID()).get();
            List<BookedRoomModel> rooms = bookedRoomRepository.findByBookingId(booking.getId());
            for (BookedRoomModel model: rooms) {
                RoomModel room = roomRepository.findById(model.getRoomID()).orElse(null);
                if (room != null) {
                    InfoBookingDTO infoDTO = new InfoBookingDTO();
                    infoDTO.setName(room.getName());
                    infoDTO.setType(room.getType());
                    infoDTO.setImage(room.getImage());
                    infoDTO.setStatus(booking.getStatus());
                    infoDTO.setNote(booking.getNote());
                    infoDTO.setCheckin(model.getCheckIn());
                    infoDTO.setCheckout(model.getCheckOut());
                    infos.add(infoDTO);
                }
            }
            dto.setId(booking.getId());
            dto.setClient(client);
            dto.setRooms(infos);
            dtos.add(dto);
        }

        return dtos;
    }

    private String getIdUserCurrent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            return null;
        }

        if (auth.getPrincipal() instanceof String) {
        return userRepository.findByUserName((String) auth.getPrincipal()).get().getId().toString();
       }

        UserDetails currentAuditor = (UserDetails) auth.getPrincipal();
        String username =  currentAuditor.getUsername();
        return userRepository.findByUserName(username).get().getId().toString();
    }
}
