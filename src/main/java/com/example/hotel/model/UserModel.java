package com.example.hotel.model;

import com.example.hotel.utils.enumm.RoleUser;
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
@Table(name = "user")
public class UserModel {
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue
    protected UUID id;

    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String userName;

    @Column(name = "position", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private RoleUser position;

    @Column(name = "password", nullable = false, length = 100)
    private String price;

    @Column(name = "full_name", length = 100)
    private String fullName;
}
