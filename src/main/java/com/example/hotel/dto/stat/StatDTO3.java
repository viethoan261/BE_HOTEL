package com.example.hotel.dto.stat;

import com.example.hotel.utils.enumm.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatDTO3 {
    private RoomType type;

    private long total;
}
