package com.example.hotel.model;

import com.example.hotel.common.model.BaseEntity;
import com.example.hotel.utils.enumm.BookingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "booking")
public class BookingModel extends BaseEntity {
    @Column(name = "booking_date")
    private LocalDateTime bookingDate;

    @Column(name = "selloff")
    private Float selloff;

    @Column(name = "note")
    private String note;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "user_id")
    @Type(type = "uuid-char")
    private UUID userID;

    @Column(name = "client_id")
    @Type(type = "uuid-char")
    private UUID clientID;
}
