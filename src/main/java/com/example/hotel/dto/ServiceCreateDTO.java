package com.example.hotel.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ServiceCreateDTO {
    @NotBlank(message = "Service name can not be blank")
    private String name;

    @Min(value = 0, message = "Min price must be 0")
    private Float price;

    private String description;
}
