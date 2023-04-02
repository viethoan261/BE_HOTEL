package com.example.hotel.dto.bill;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RoomBillDTO {
    private String name;

    private Float price;

    private Float saleoff;

    List<ServiceBillDTO> services;
}
