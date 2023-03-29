package com.example.hotel.repository;

import com.example.hotel.model.RoomModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<RoomModel, UUID> {
    @Query("select r FROM RoomModel r where r.name = :name and r.status <> 'BLOCK'")
    Optional<RoomModel> findRoomByName(String name);

    @Query("select r FROM RoomModel r where r.id IN :ids and r.status <> 'BLOCK'")
    List<RoomModel> findRoomByIds(List<UUID> ids);
}
