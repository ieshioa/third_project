package com.green.glampick.repository;

import com.green.glampick.entity.RoomEntity;
import com.green.glampick.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    @Query("select s.serviceId from ServiceEntity s join RoomServiceEntity rs on s = rs.service" +
            " where rs.room = :room")
    List<Long> findRoomServiceIdByRoom(RoomEntity room);


}
