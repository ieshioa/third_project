package com.green.glampick.repository;

import com.green.glampick.entity.RoomEntity;
import com.green.glampick.entity.RoomServiceEntity;
import com.green.glampick.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomServiceRepository extends JpaRepository<RoomServiceEntity, Long> {
    RoomServiceEntity findByServiceAndRoom(ServiceEntity service, RoomEntity room);
    void deleteAllByRoom(RoomEntity room);
}
