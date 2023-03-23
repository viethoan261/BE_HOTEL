package com.example.hotel.security.dto;

import com.example.hotel.security.validation.NotExistedUser;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterDTO {
    @NotBlank(message = "Username can not be blank")
    @NotExistedUser(message = "Username is existed")
    private String username;

    @NotBlank(message = "Password can not be blank")
    private String password;
}

