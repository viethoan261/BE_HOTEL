package com.example.hotel.repository;

import com.example.hotel.dto.stat.StatDTO;
import com.example.hotel.dto.stat.StatDTO2;
import com.example.hotel.model.BillModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<BillModel, UUID> {
    @Query("select new com.example.hotel.dto.stat.StatDTO2(( MONTH(b.createdAt)), sum(b.amount)) from BillModel b  group by MONTH(b.createdAt)")
    List<StatDTO2> getBillStat();
}
