package com.example.hotel.dto;

import com.example.hotel.utils.enumm.BookingStatus;
import com.example.hotel.utils.enumm.RoomType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class InfoBookingDTO {
    private UUID id;
    private String name;
    private LocalDateTime checkin;
    private LocalDateTime checkout;
    private String note;
    private RoomType type;
    private String image;
}
