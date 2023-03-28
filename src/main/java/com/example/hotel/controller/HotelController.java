package com.example.hotel.controller;

import com.example.hotel.common.util.ResponseHelper;
import com.example.hotel.dto.CreateRoomDTO;
import com.example.hotel.model.RoomModel;
import com.example.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = {"http://localhost:5173/", "http://localhost:5174/"})
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @PostMapping("rooms")
    public Object create(@Valid @RequestBody CreateRoomDTO dto,
                         BindingResult result) {
        if(result.hasErrors()) {
            return ResponseHelper.getErrorResponse(result, HttpStatus.BAD_REQUEST);
        }

        CreateRoomDTO newRoom = hotelService.create(dto);

        return ResponseHelper.getResponse(newRoom, HttpStatus.CREATED);
    }

    @PostMapping("rooms/{id}")
    public Object update(String id, @Valid @RequestBody CreateRoomDTO dto,
                         BindingResult result) {
        if(result.hasErrors()) {
            return ResponseHelper.getErrorResponse(result, HttpStatus.BAD_REQUEST);
        }

        RoomModel updateRoom = hotelService.update(UUID.fromString(id), dto);

        if (updateRoom == null) {
            return ResponseHelper.getErrorResponse("Room is not existed", HttpStatus.BAD_REQUEST);
        }

        return ResponseHelper.getResponse(updateRoom, HttpStatus.OK);
    }

    @GetMapping("/rooms/{booking-id}/approve")
    public Object approve(@PathVariable(name = "booking-id") String id, Float selloff) {
        hotelService.approve(UUID.fromString(id), selloff);
        return ResponseHelper.getResponse("Dat phong thanh cong", HttpStatus.OK);
    }

    @GetMapping("/rooms/{booking-id}/cancel")
    public Object cancel(@PathVariable(name = "booking-id") String id) {
        hotelService.cancel(UUID.fromString(id));
        return ResponseHelper.getResponse("Huy dat phong thanh cong", HttpStatus.OK);
    }

    @GetMapping("/rooms/{booking-id}/checkin")
    public Object checkin(@PathVariable(name = "booking-id") String id) {
        hotelService.checkin(UUID.fromString(id));
        return ResponseHelper.getResponse("checkin thanh cong", HttpStatus.OK);
    }

    @GetMapping("/rooms/bookings")
    public Object getBooking() {
        return ResponseHelper.getResponse(hotelService.getBooking(), HttpStatus.OK);
    }
}
