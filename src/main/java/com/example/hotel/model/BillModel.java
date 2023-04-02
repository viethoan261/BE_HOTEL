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
@Table(name = "bill")
public class BillModel extends BaseEntity {
    @Column(name = "amount", nullable = false)
    private Float amount;

    @Column(name = "user_id")
    @Type(type = "uuid-char")
    private UUID userID;

    @Column(name = "booking_id")
    @Type(type = "uuid-char")
    private UUID bookingID;
}
