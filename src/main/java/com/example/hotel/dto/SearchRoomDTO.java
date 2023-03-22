package com.example.hotel.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SearchRoomDTO {
    private LocalDateTime checkIn;

    private LocalDateTime checkOut;
}
