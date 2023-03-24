package com.example.hotel.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class RequestBookRoomDTO {
    @NotBlank(message = "ID card can not be blank")
    private String idCard;

    private String address;

    @NotBlank(message = "Tel can not be blank")
    private String tel;

    private String email;

    private String note;

    @NotBlank(message = "Full name can not be blank")
    private String fullName;

    @NotBlank(message = "Bank name can not be blank")
    private String bankName;

    @NotBlank(message = "Bank number can not be blank")
    private String bankNumber;

    @NotNull(message = "Ids room can not be null")
    private List<String> idsRoom;

    @NotNull(message = "Time checkin can not be null")
    private LocalDateTime checkIn;

    @NotNull(message = "Time checkout can not be null")
    private LocalDateTime checkOut;
}
