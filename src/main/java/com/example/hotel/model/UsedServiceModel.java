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
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "used_service")
public class UsedServiceModel extends BaseEntity {
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "bookied_room_id", nullable = false)
    @Type(type = "uuid-char")
    private UUID bookiedRoomID;

    @Column(name = "booking_id", nullable = false)
    @Type(type = "uuid-char")
    private UUID bookingID;

    @Column(name = "service_id", nullable = false)
    @Type(type = "uuid-char")
    private UUID serviceID;

    @Column(name = "saleoff")
    private Float saleoff;
}
