package com.example.hotel.controller;

import com.example.hotel.common.util.ResponseHelper;
import com.example.hotel.dto.CreateRoomDTO;
import com.example.hotel.dto.RequestBookRoomDTO;
import com.example.hotel.dto.SearchRoomDTO;
import com.example.hotel.dto.TestDTO;
import com.example.hotel.model.RoomModel;
import com.example.hotel.service.ClientService;
import com.example.hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/v2")
@CrossOrigin(origins = "http://localhost:5173/")
public class ClientController {
    @Autowired
    private ClientService service;

//    @PostMapping("{id}/test")
//    public Object createTest(@PathVariable  String id, @Valid @RequestBody TestDTO dto) {
//
//        TestDTO dtoTest = roomService.test(UUID.fromString(id), dto);
//
//        return ResponseHelper.getResponse(dtoTest, HttpStatus.OK);
//    }

    @PostMapping("search")
    public Object search(@RequestBody SearchRoomDTO dto) {
        return ResponseHelper.getResponse(service.search(dto), HttpStatus.OK);
    }

    @PostMapping("order")
    public Object order(@Valid @RequestBody RequestBookRoomDTO dto, BindingResult result) {

        if(result.hasErrors()) {
            return ResponseHelper.getErrorResponse(result, HttpStatus.BAD_REQUEST);
        }

        RequestBookRoomDTO dtoRoom = service.orderRoom(dto);

        if (dtoRoom == null) {
            return ResponseHelper.getErrorResponse("Can not order room", HttpStatus.BAD_REQUEST);
        }

        return ResponseHelper.getResponse(dtoRoom, HttpStatus.OK);
    }
}
