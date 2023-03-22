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
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "client")
public class ClientModel {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue
    protected UUID id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "id_card", nullable = false)
    private String idCard;

    @Column(name = "address")
    private String address;

    @Column(name = "tel", nullable = false)
    private String tel;

    @Column(name = "email")
    private String email;

    @Column(name = "note")
    private String note;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed;
}
