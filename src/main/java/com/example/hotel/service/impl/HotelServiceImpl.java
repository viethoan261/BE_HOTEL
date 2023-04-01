package com.example.hotel.service.impl;

import com.example.hotel.dto.BookingListDTO;
import com.example.hotel.dto.CreateRoomDTO;
import com.example.hotel.dto.InfoBookingDTO;
import com.example.hotel.dto.ServiceCreateDTO;
import com.example.hotel.dto.TestDTO;
import com.example.hotel.dto.servicedto.OrderServiceDTO;
import com.example.hotel.dto.servicedto.OrderServiceResponse;
import com.example.hotel.dto.servicedto.ServiceDTO;
import com.example.hotel.mapper.RoomMapper;
import com.example.hotel.model.BookedRoomModel;
import com.example.hotel.model.BookingModel;
import com.example.hotel.model.ClientModel;
import com.example.hotel.model.RoomModel;
import com.example.hotel.model.ServiceModel;
import com.example.hotel.model.UsedServiceModel;
import com.example.hotel.repository.BookedRoomRepository;
import com.example.hotel.repository.BookingRepository;
import com.example.hotel.repository.ClientRepository;
import com.example.hotel.repository.RoomRepository;
import com.example.hotel.repository.ServiceRepository;
import com.example.hotel.repository.UsedServiceRepository;
import com.example.hotel.repository.UserRepository;
import com.example.hotel.service.HotelService;
import com.example.hotel.utils.enumm.BookingStatus;
import com.example.hotel.utils.enumm.RoomBookedStatus;
import com.example.hotel.utils.enumm.RoomStatus;
import com.example.hotel.utils.enumm.ServiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private UsedServiceRepository usedServiceRepository;

    @Transactional
    @Override
    public CreateRoomDTO create(CreateRoomDTO dto) {
        RoomModel room = RoomMapper.INSTANCE.from(dto);
        room.setIsBooked(Boolean.FALSE);
        room.setStatus(RoomStatus.FREE);
        roomRepository.save(room);
        return dto;
    }

    @Transactional
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
    public RoomModel blockRoom(UUID id) {
        Optional<RoomModel> roomOpt = roomRepository.findById(id);

        if (roomOpt.isEmpty()) {
            return null;
        }
        RoomModel room = roomOpt.get();
        if (RoomStatus.FREE.equals(room.getStatus())) {
            room.setStatus(RoomStatus.BLOCK);
            return roomRepository.save(room);
        }
        return null;
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
        booking.setStatus(BookingStatus.PROGRESS);
        bookingRepository.save(booking);

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
                    infoDTO.setId(room.getId());
                    infoDTO.setName(room.getName());
                    infoDTO.setType(room.getType());
                    infoDTO.setImage(room.getImage());
                    infoDTO.setNote(booking.getNote());
                    infoDTO.setCheckin(model.getCheckIn());
                    infoDTO.setCheckout(model.getCheckOut());
                    infos.add(infoDTO);
                }
            }
            dto.setId(booking.getId());
            dto.setClient(client);
            dto.setRooms(infos);
            dto.setStatus(booking.getStatus());
            dtos.add(dto);
        }

        return dtos;
    }

    @Transactional
    @Override
    public ServiceModel createService(ServiceCreateDTO dto) {
        ServiceModel model = new ServiceModel();
        model.setName(dto.getName());
        model.setPrice(dto.getPrice());
        model.setStatus(ServiceStatus.ACTIVE);
        if (dto.getDescription() != null) {
            model.setDescription(dto.getDescription());
        }
        return serviceRepository.save(model);
    }

    @Transactional

    @Override
    public ServiceModel updateService(UUID serviceID, ServiceCreateDTO dto) {
        Optional<ServiceModel> model = serviceRepository.findById(serviceID);
        if (model.isPresent()) {
            ServiceModel modelUpdate = model.get();
            modelUpdate.setName(dto.getName());
            modelUpdate.setPrice(dto.getPrice());
            if (dto.getDescription() != null) {
                modelUpdate.setDescription(dto.getDescription());
            }
            return serviceRepository.save(modelUpdate);
        }
        return null;
    }

    @Override
    public ServiceModel inactiveService(UUID serviceID) {
        Optional<ServiceModel> model = serviceRepository.findById(serviceID);
        if (model.isPresent()) {
            ServiceModel modelUpdate = model.get();
            modelUpdate.setStatus(ServiceStatus.INACTIVE);
            return serviceRepository.save(modelUpdate);
        }
        return null;
    }

    @Override
    public List<ServiceModel> getAllService() {
        return serviceRepository.getAllService();
    }

    @Transactional
    @Override
    public OrderServiceResponse orderService(UUID bookingID, List<OrderServiceDTO> dtos) {
        Optional<BookingModel> bookingModel = bookingRepository.findById(bookingID);
        if (bookingModel.isEmpty()) {
            return null;
        }

        BookingModel booking = bookingModel.get();
        if (!BookingStatus.PROGRESS.equals(booking.getStatus())) {
            return null;
        }

        OrderServiceResponse response = new OrderServiceResponse();
        List<UsedServiceModel> usedServiceModels = new ArrayList<>();

        List<UUID> roomIds = dtos.stream().map(t -> t.getRoomID()).collect(Collectors.toList());
        if (roomIds == null) {
            return null;
        }

        for (OrderServiceDTO dto:
             dtos) {
            UsedServiceModel usedService = new UsedServiceModel();
            List<ServiceDTO> serviceDTOS = dto.getServices();
            if (serviceDTOS != null) {
                Float price = 0f;
                for (ServiceDTO serviceDTO: serviceDTOS
                     ) {
                    ServiceModel service = serviceRepository.getById(serviceDTO.getId());
                    if (service != null) {
                        usedService.setServiceID(serviceDTO.getId());
                        usedService.setQuantity(serviceDTO.getQuantity());
                        usedService.setSelloff(serviceDTO.getSelloff());
                        price = price + serviceDTO.getQuantity() * service.getPrice() * (100 - serviceDTO.getSelloff()) / 100;
                        usedService.setPrice(price);
                    }
                }
                usedService.setPrice(price);
            }
            usedService.setBookingID(booking.getId());
            usedService.setBookiedRoomID(dto.getRoomID());
            usedServiceModels.add(usedService);
        }

        usedServiceRepository.saveAll(usedServiceModels);

        response.setBookingID(booking.getId());
        response.setServices(dtos);

        return response;
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
