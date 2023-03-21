package com.example.hotel.model;

import com.example.hotel.utils.enumm.RoomType;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class UserModel {
    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "type", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private RoomType type;

    @Column(name = "price", nullable = false, length = 100)
    private Float price;

    @Column(name = "username", nullable = true, length = 100)
    private String description;
}
