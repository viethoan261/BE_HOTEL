package com.example.hotel.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "booked_room")
public class BookedRoomModel {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue
    protected UUID id;

    @Column(name = "room_id", nullable = false)
    @Type(type = "uuid-char")
    private UUID roomID;

    @Column(name = "check_in", nullable = false)
    private LocalDateTime checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDateTime checkOut;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "note")
    private String note;

    @Column(name = "selloff", nullable = false)
    private Float selloff;

    @Column(name = "is_check_in")
    private Boolean isCheckIn;

    @Column(name = "booking_id")
    @Type(type = "uuid-char")
    private UUID bookingId;
}
