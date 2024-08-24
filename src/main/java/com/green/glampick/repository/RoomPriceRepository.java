package com.green.glampick.repository;

import com.green.glampick.dto.object.owner.OwnerRoomPriceItem;
import com.green.glampick.entity.RoomEntity;
import com.green.glampick.entity.RoomPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomPriceRepository extends JpaRepository<RoomPriceEntity, Long> {
    @Query( "select rp.room.roomId AS roomId, rp.weekdayPrice AS weekdayPrice, rp.weekendPrice AS weekendPrice " +
            "from RoomPriceEntity rp " +
            "join rp.room r " +
            "where r.glamp.glampId = :glampId "
    )
    List<OwnerRoomPriceItem> getRoomPriceList(@Param("glampId") Long glampId);

    @Query("SELECT rp FROM RoomPriceEntity rp WHERE rp.room.roomId = :roomId")
    RoomPriceEntity findRoomPriceByRoomId(@Param("roomId") Long roomId);

    RoomPriceEntity findByRoom(RoomEntity room);


}
