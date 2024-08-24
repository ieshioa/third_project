package com.green.glampick.mapper;

import com.green.glampick.dto.object.room.RoomInfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JsonInputMapper {
    Long selGlamp(String glampName);
    int insRoomData(RoomInfoDto roomInfoDto);

    List<String> getRoomName();
    int insertRoomImg(RoomInfoDto roomInfoDto);
    RoomInfoDto getRoomId(String roomName);
}
