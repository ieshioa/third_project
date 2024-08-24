package com.green.glampick.repository;

import com.green.glampick.entity.RoomEntity;
import com.green.glampick.entity.RoomImageEntity;
import com.green.glampick.repository.resultset.GetRoomImgInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomImageRepository extends JpaRepository<RoomImageEntity, Long> {
    List<RoomImageEntity> findByRoomId(RoomEntity room);

    @Query ("select ri.roomImageId as id, ri.roomImageName as name from RoomImageEntity ri where ri.roomId = :room")
    List<GetRoomImgInfo> getRoomImg(RoomEntity room);

}
