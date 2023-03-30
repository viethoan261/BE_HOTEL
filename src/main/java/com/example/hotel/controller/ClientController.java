package com.example.hotel.controller;

import com.example.hotel.common.util.ResponseHelper;
import com.example.hotel.dto.RequestBookRoomDTO;
import com.example.hotel.dto.SearchRoomDTO;
import com.example.hotel.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v2")
@CrossOrigin(origins = {"http://localhost:5173/", "http://localhost:5174/"})
public class ClientController {
    @Autowired
    private ClientService service;

    @Operation(summary = "Get all room available ")
    @PostMapping("search")
    public Object search(@RequestBody SearchRoomDTO dto) {
        return ResponseHelper.getResponse(service.search(dto), HttpStatus.OK);
    }

    @Operation(summary = "Order room")
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
