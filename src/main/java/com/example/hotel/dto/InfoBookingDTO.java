package com.example.hotel.dto;

import com.example.hotel.utils.enumm.BookingStatus;
import com.example.hotel.utils.enumm.RoomType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class InfoBookingDTO {
    private String name;
    private LocalDateTime checkin;
    private LocalDateTime checkout;
    private String note;
    private RoomType type;
    private String image;
}
