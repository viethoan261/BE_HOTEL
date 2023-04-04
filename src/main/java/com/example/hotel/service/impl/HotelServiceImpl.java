package com.example.hotel.service.impl;

import com.example.hotel.dto.*;
import com.example.hotel.dto.bill.InfoBillDTO;
import com.example.hotel.dto.bill.RoomBillDTO;
import com.example.hotel.dto.bill.ServiceBillDTO;
import com.example.hotel.dto.servicedto.OrderServiceDTO;
import com.example.hotel.dto.servicedto.OrderServiceResponse;
import com.example.hotel.dto.servicedto.ServiceDTO;
import com.example.hotel.dto.stat.StatDTO;
import com.example.hotel.dto.stat.StatDTO2;
import com.example.hotel.mapper.RoomMapper;
import com.example.hotel.model.*;
import com.example.hotel.repository.*;
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

    @Autowired
    private BillRepository billRepository;

    @Transactional
    @Override
    public CreateRoomDTO create(CreateRoomDTO dto) {
        RoomModel room = RoomMapper.INSTANCE.from(dto);
        room.setIsBooked(Boolean.FALSE);
        room.setStatus(RoomStatus.FREE);
        roomRepository.save(room);
        return dto;
    }

    @Override
    public List<RoomModel> getAllRoom() {
        List<RoomModel> rooms = roomRepository.findAll();
        return rooms;
    }

    @Transactional
    @Override
    public RoomModel update(UUID id, UpdateRoomDTO dto) {
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
    public void approve(UUID bookingID, Float saleoff) {
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
        booking.setSaleoff(saleoff);
        bookingRepository.save(booking);

        //update booked room
        List<BookedRoomModel> bookedRoomModels = bookedRoomRepository.findByBookingId(bookingID);
        for (BookedRoomModel bookedRoom: bookedRoomModels
             ) {
            bookedRoom.setSaleoff(saleoff);
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

            List<ServiceDTO> serviceDTOS = dto.getServices();
            if (serviceDTOS != null) {
                Float price = 0f;
                for (ServiceDTO serviceDTO: serviceDTOS
                     ) {
                    ServiceModel service = serviceRepository.getById(serviceDTO.getId());
                    if (service != null) {
                        UsedServiceModel usedService = new UsedServiceModel();
                        usedService.setServiceID(serviceDTO.getId());
                        usedService.setQuantity(serviceDTO.getQuantity());
                        usedService.setSaleoff(serviceDTO.getSaleoff());
                        price = serviceDTO.getQuantity() * service.getPrice();
                        usedService.setPrice(price);
                        usedService.setBookingID(booking.getId());
                        usedService.setBookiedRoomID(dto.getRoomID());
                        usedServiceModels.add(usedService);
                    }
                }
//                usedService.setPrice(price);
            }
//            usedService.setBookingID(booking.getId());
//            usedService.setBookiedRoomID(dto.getRoomID());
//            usedServiceModels.add(usedService);
        }

        usedServiceRepository.saveAll(usedServiceModels);

        response.setBookingID(booking.getId());
        response.setServices(dtos);

        return response;
    }

    @Transactional
    @Override
    public InfoBillDTO payment(UUID bookingID) {
        Float deposit;
        Float totalRoom = 0f;
        Float serviceCost = 0f;
        Float amount;
        BookingModel booking = bookingRepository.findById(bookingID).orElse(null);
        if (booking == null) {
            return null;
        }

        List<BookedRoomModel> bookedRoomModels = bookedRoomRepository.findByBookingId(bookingID);
        InfoBillDTO bill = new InfoBillDTO();
        ClientModel client = clientRepository.findById(booking.getClientID()).orElse(null);
        UserModel user = userRepository.getById(booking.getUserID());
        List<RoomBillDTO> roomBillDTOS = new ArrayList<>();
        for (BookedRoomModel roomModel:
                bookedRoomModels) {
            totalRoom = totalRoom + roomModel.getPrice() * (1 - roomModel.getSaleoff() / 100);

            RoomBillDTO dto = new RoomBillDTO();
            List<ServiceBillDTO> serviceBillDTOS = new ArrayList<>();
            List<UsedServiceModel> usedServiceModels = usedServiceRepository.findByBookingIDAndRoomID(booking.getId(), roomModel.getRoomID());

            for (UsedServiceModel model : usedServiceModels) {
                ServiceBillDTO serviceBillDTO = new ServiceBillDTO();
                ServiceModel serviceModel = serviceRepository.getById(model.getServiceID());
                serviceCost = serviceCost + model.getPrice() * (1 - model.getSaleoff() / 100);
                if (model.getSaleoff() != null) {
                    serviceBillDTO.setSaleoff(model.getSaleoff());
                }
                serviceBillDTO.setQuantity(model.getQuantity());
                serviceBillDTO.setPrice(model.getPrice());
                serviceBillDTO.setName(serviceModel.getName());
                serviceBillDTOS.add(serviceBillDTO);
            }
            
            RoomModel roomModel1 = roomRepository.getById(roomModel.getRoomID());
            dto.setName(roomModel1.getName());
            dto.setPrice(roomModel1.getPrice());
            if (roomModel.getSaleoff() != null) {
                dto.setSaleoff(roomModel.getSaleoff());
            }
            dto.setServices(serviceBillDTOS);

            roomBillDTOS.add(dto);
        }

        deposit = totalRoom / 2;
        amount = totalRoom - deposit + serviceCost;

        bill.setClient(client);
        bill.setEmployee(user.getUserName());
        bill.setRooms(roomBillDTOS);
        bill.setDeposit(deposit);
        bill.setAmount(amount);

        if (!BookingStatus.DONE.equals(booking.getStatus())) {
            booking.setStatus(BookingStatus.DONE);
            booking.setUserID(UUID.fromString(this.getIdUserCurrent()));
            bookingRepository.save(booking);

            for (BookedRoomModel model:
                 bookedRoomModels) {
                model.setStatus(RoomBookedStatus.DONE);
            }
            bookedRoomRepository.saveAll(bookedRoomModels);

            List<UUID> roomIds = bookedRoomModels.stream().map(t -> t.getRoomID()).collect(Collectors.toList());
            List<RoomModel> roomModels = roomRepository.findRoomByIds(roomIds);
            for (RoomModel model:
                    roomModels) {
                model.setStatus(RoomStatus.FREE);
                model.setIsBooked(Boolean.FALSE);
            }
            roomRepository.saveAll(roomModels);

            BillModel billModel = new BillModel();
            billModel.setAmount(amount);
            billModel.setBookingID(bookingID);
            billModel.setUserID(user.getId());
            billRepository.save(billModel);
        }
        return bill;
    }

    @Override
    public List<StatDTO> statService() {
        return serviceRepository.getStat();
    }

    @Override
    public List<StatDTO> statRoom() {
        return bookedRoomRepository.getRoomStat();
    }

    @Override
    public List<StatDTO2> statBill() {
        return billRepository.getBillStat();
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
