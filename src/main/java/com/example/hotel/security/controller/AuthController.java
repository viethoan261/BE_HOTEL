package com.example.hotel.security.controller;

import com.example.hotel.common.util.ResponseHelper;
import com.example.hotel.security.dto.LoginDTO;
import com.example.hotel.security.dto.RegisterDTO;
import com.example.hotel.security.serivce.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @PostMapping("login")
    public Object login(@RequestBody @Valid LoginDTO dto, BindingResult bindingResult) {

        if(bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        String token = service.login(dto);
        if(token == null) {
            return ResponseHelper.getErrorResponse("Password is not correct.", HttpStatus.BAD_REQUEST);
        } else {
            return ResponseHelper.getResponse(token, HttpStatus.OK);
        }

    }

    @PostMapping("register")
    public Object register(@RequestBody @Valid RegisterDTO dto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        }
        Object user = service.register(dto);

        return ResponseHelper.getResponse(user, HttpStatus.OK);
    }
}
