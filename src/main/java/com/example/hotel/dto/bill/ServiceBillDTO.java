package com.example.hotel.dto.bill;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceBillDTO {
    private String name;

    private Float price;

    private Integer quantity;

    private Float saleoff;
}
