package com.green.glampick.dto.object.room;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class RoomInfoDto {
    private String name;
    private int glampId;
    private int price;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private int numPeople;
    private int maxPeople;


    private Long roomId;
    private List<String> roomImgName;
}
/**
 * Room Name: VIP패밀리카라반3
 * Room Price: 119000
 * Room checkIn: 15:00
 * Room checkOut: 11:00
 * Room numPeople: 2
 * Room maxPeople: 4
 *
 * // JSON 데이터 파싱
 * JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
 * String checkInTimeStr = (String) jsonObject.get("check_in_time");
 *
 * // JSON 문자열을 LocalTime으로 변환
 * DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
 * LocalTime checkInTime = LocalTime.parse(checkInTimeStr, formatter);
 *
 *
 *
 *
 */
