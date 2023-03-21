package com.example.hotel.dto;

import com.example.hotel.utils.enumm.RoomType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class CreateRoomDTO {
    @NotBlank
    private String name;

    @NotNull
    private RoomType type;

    @Min(0)
    private Float price;

    private String description;
}
