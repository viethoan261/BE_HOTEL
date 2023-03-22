package com.example.hotel.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TestDTO {

    private LocalDateTime checkIn;

    private LocalDateTime checkOut;

    private Float price;

    private String note;
}
