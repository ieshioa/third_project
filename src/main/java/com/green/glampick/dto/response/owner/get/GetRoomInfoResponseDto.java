package com.green.glampick.dto.response.owner.get;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.room.RoomImageItem;
import com.green.glampick.entity.RoomPriceEntity;
import com.green.glampick.repository.resultset.GetRoomInfoResultSet;
import com.green.glampick.repository.resultset.OwnerInfoResultSet;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;

@Getter
public class GetRoomInfoResponseDto extends ResponseDto {

    @Schema(example = "카라반 102호", description = "객실 이름")
    private String roomName;

    @Schema(example = "65500", description = "주중 객실 가격")
    private Integer weekdayPrice;

    @Schema(example = "65500", description = "주말 객실 가격")
    private Integer weekendPrice;

    @Schema(example = "2", description = "기준 인원")
    private Integer peopleNum;

    @Schema(example = "6", description = "최대 인원")
    private Integer peopleMax;

    @Schema(example = "15:00:00", description = "입실 시간")
    private String inTime;

    @Schema(example = "12:00:00", description = "퇴실 시간")
    private String outTime;

    // 객실 PK와 이름
    private List<RoomImageItem> roomImg;

    @Schema(example = "[1,2,3]", description = "객실 서비스")
    private List<Long> service;

    public GetRoomInfoResponseDto(String roomName, Integer weekdayPrice, Integer weekendPrice, Integer peopleNum,
                                  Integer peopleMax, String inTime, String outTime, List<RoomImageItem> roomImg,
                                  List<Long> service){
        super(SUCCESS_CODE, "객실 상세 정보를 불러왔습니다.");
        this.roomName=roomName;
        this.weekdayPrice=weekdayPrice;
        this.weekendPrice=weekendPrice;
        this.peopleNum=peopleNum;
        this.peopleMax=peopleMax;
        this.inTime=inTime;
        this.outTime=outTime;
        this.roomImg=roomImg;
        this.service=service;
    }

    public static ResponseEntity<GetRoomInfoResponseDto> success(GetRoomInfoResultSet resultSet, List<RoomImageItem> roomImg
            , List<Long> service, RoomPriceEntity roomPrice) {
        GetRoomInfoResponseDto result = new GetRoomInfoResponseDto(resultSet.getRoomName(), roomPrice.getWeekdayPrice()
                , roomPrice.getWeekendPrice(), resultSet.getPeopleNum(), resultSet.getPeopleMax()
                , resultSet.getInTime(), resultSet.getOutTime(), roomImg, service);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
