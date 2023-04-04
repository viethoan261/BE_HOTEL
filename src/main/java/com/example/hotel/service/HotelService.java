package com.example.hotel.service;

import com.example.hotel.dto.*;
import com.example.hotel.dto.bill.InfoBillDTO;
import com.example.hotel.dto.servicedto.OrderServiceDTO;
import com.example.hotel.dto.servicedto.OrderServiceResponse;
import com.example.hotel.dto.stat.StatDTO;
import com.example.hotel.dto.stat.StatDTO2;
import com.example.hotel.model.RoomModel;
import com.example.hotel.model.ServiceModel;

import java.util.List;
import java.util.UUID;

public interface HotelService {
    CreateRoomDTO create(CreateRoomDTO dto);

    List<RoomModel> getAllRoom();

    RoomModel update(UUID id, UpdateRoomDTO dto);

    RoomModel blockRoom(UUID id);

    void approve(UUID bookingID, Float saleoff);

    void cancel(UUID bookingID);

    void checkin(UUID bookingID);

    List<BookingListDTO> getBooking();

    ServiceModel createService(ServiceCreateDTO dto);

    ServiceModel updateService(UUID serviceID, ServiceCreateDTO dto);

    ServiceModel inactiveService(UUID serviceID);

    List<ServiceModel> getAllService();

    //service
    OrderServiceResponse orderService(UUID bookingID, List<OrderServiceDTO> dtos);

    InfoBillDTO payment(UUID bookingID);

    //stat
    List<StatDTO> statService();

    List<StatDTO> statRoom();

    List<StatDTO2> statBill();
}
