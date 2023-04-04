package com.example.hotel.dto.servicedto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ServiceDTO {
    @NotNull(message = "Service ID can not be null")
    private UUID id;

    @Min(value = 1, message = "Min quantity must be 1")
    private Integer quantity;

    @Min(value = 0, message = "Saleoff Min must be 1")
    private Float saleoff;
}
