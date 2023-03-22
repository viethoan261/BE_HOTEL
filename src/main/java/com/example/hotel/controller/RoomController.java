package com.example.hotel.controller;

import com.example.hotel.common.util.ResponseHelper;
import com.example.hotel.dto.CreateRoomDTO;
import com.example.hotel.dto.SearchRoomDTO;
import com.example.hotel.dto.TestDTO;
import com.example.hotel.model.RoomModel;
import com.example.hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/rooms")
@CrossOrigin(origins = "http://localhost:5173/")
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

    @PostMapping("{id}")
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

    @PostMapping("{id}/test")
    public Object createTest(@PathVariable  String id, @Valid @RequestBody TestDTO dto) {

        TestDTO dtoTest = roomService.test(UUID.fromString(id), dto);

        return ResponseHelper.getResponse(dtoTest, HttpStatus.OK);
    }

    @PostMapping("search")
    public Object search(@RequestBody SearchRoomDTO dto) {
        return ResponseHelper.getResponse(roomService.search(dto), HttpStatus.OK);
    }
}
