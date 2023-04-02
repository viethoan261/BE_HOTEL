package com.example.hotel.dto.bill;

import com.example.hotel.model.ClientModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class InfoBillDTO {
    private ClientModel client;

    private String employee;

    private List<RoomBillDTO> rooms;

    private Float deposit;

    private Float amount;
}
