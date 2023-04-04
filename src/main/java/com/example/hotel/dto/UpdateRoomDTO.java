package com.example.hotel.dto;

import com.example.hotel.utils.enumm.RoomType;
import com.example.hotel.validation.annotation.UniqueRoomName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class UpdateRoomDTO {
    @NotBlank(message = "Room name can not be blank")
    private String name;

    @NotNull(message = "Room type can not be null")
    private RoomType type;

    @Min(value = 0, message = "Min price must be 0")
    private Float price;

    private String description;

    private String image;
}
