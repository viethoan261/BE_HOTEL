package com.example.hotel.security.serivce;

import com.example.hotel.security.dto.LoginDTO;
import com.example.hotel.security.dto.RegisterDTO;

public interface AuthService {
    String login(LoginDTO dto);
    Object register(RegisterDTO dto);

}
