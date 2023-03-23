package com.example.hotel.model;

import com.example.hotel.common.model.BaseEntity;
import com.example.hotel.utils.enumm.RoomStatus;
import com.example.hotel.utils.enumm.RoomType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "room")
public class RoomModel extends BaseEntity{
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "type", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private RoomType type;

    @Column(name = "price", nullable = false, length = 100)
    private Float price;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "is_booked")
    private Boolean isBooked;

    @Column(name = "image")
    private String image;

    @Column(name = "status", length = 100)
    @Enumerated(EnumType.STRING)
    private RoomStatus status;
}
