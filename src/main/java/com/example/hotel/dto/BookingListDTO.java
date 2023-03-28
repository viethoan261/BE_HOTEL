package com.example.hotel.dto;

import com.example.hotel.model.ClientModel;
import com.example.hotel.model.RoomModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
public class BookingListDTO {
    private UUID id;
    private ClientModel client;
    private List<InfoBookingDTO> rooms;
}
