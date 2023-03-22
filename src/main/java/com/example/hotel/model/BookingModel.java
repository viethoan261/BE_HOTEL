package com.example.hotel.model;

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
public class BookingModel {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue
    protected UUID id;

    @Column(name = "booking_date")
    private LocalDateTime bookingDate;

    @Column(name = "selloff")
    private Float selloff;

    @Column(name = "note")
    private String note;

    @Column(name = "user_id")
    @Type(type = "uuid-char")
    private UUID userID;

    @Column(name = "client_id")
    @Type(type = "uuid-char")
    private UUID clientID;
}
