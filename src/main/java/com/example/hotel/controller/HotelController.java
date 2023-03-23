package com.example.hotel.controller;

import com.example.hotel.common.util.ResponseHelper;
import com.example.hotel.dto.CreateRoomDTO;
import com.example.hotel.model.RoomModel;
import com.example.hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:5173/")
public class HotelController {
    @Autowired
    private RoomService roomService;

    @PostMapping("rooms")
    public Object create(@Valid @RequestBody CreateRoomDTO dto,
                         BindingResult result) {
        if(result.hasErrors()) {
            return ResponseHelper.getErrorResponse(result, HttpStatus.BAD_REQUEST);
        }

        CreateRoomDTO newRoom = roomService.create(dto);

        return ResponseHelper.getResponse(newRoom, HttpStatus.CREATED);
    }

    @PostMapping("rooms/{id}")
    public Object update(String id, @Valid @RequestBody CreateRoomDTO dto,
                         BindingResult result) {
        if(result.hasErrors()) {
            return ResponseHelper.getErrorResponse(result, HttpStatus.BAD_REQUEST);
        }

        RoomModel updateRoom = roomService.update(UUID.fromString(id), dto);

        if (updateRoom == null) {
            return ResponseHelper.getErrorResponse("Room is not existed", HttpStatus.BAD_REQUEST);
        }

        return ResponseHelper.getResponse(updateRoom, HttpStatus.OK);
    }
}
