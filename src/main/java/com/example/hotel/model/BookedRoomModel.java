package com.example.hotel.model;

import com.example.hotel.common.model.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "booked_room")
public class BookedRoomModel extends BaseEntity {
    @Column(name = "room_id", nullable = false)
    private String roomID;

    @Column(name = "check_in", nullable = false)
    private LocalDateTime checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDateTime checkOut;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "note")
    private String note;

    @Column(name = "amount", nullable = false)
    private Float amount;

    @Column(name = "service_id")
    private String serviceId;
}
