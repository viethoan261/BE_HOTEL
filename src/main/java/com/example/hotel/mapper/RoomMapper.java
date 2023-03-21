package com.example.hotel.mapper;

import com.example.hotel.dto.CreateRoomDTO;
import com.example.hotel.model.RoomModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoomMapper {
    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    RoomModel from(CreateRoomDTO dto);
}
