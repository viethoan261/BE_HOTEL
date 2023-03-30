package com.example.hotel.dto.servicedto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderServiceResponse {
    private UUID bookingID;
    private List<OrderServiceDTO> services;
}
