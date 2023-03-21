package com.example.hotel.controller;

import com.example.hotel.common.util.ResponseHelper;
import com.example.hotel.dto.CreateRoomDTO;
import com.example.hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PostMapping
    public Object create(@Valid @RequestBody CreateRoomDTO dto,
                         BindingResult result) {
        if(result.hasErrors()) {
            return ResponseHelper.getErrorResponse(result, HttpStatus.BAD_REQUEST);
        }

        CreateRoomDTO newRoom = roomService.create(dto);

        return ResponseHelper.getResponse(newRoom, HttpStatus.CREATED);
    }
}
