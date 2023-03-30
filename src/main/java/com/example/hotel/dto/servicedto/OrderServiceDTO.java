package com.example.hotel.dto.servicedto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderServiceDTO {
    @NotNull(message = "Room ID can not be null")
    private UUID roomID;

    @Valid
    @NotNull(message = "Service can not be null")
    private List<ServiceDTO> services;
}
