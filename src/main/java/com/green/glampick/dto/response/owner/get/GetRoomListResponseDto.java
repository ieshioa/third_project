package com.green.glampick.dto.response.owner.get;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.owner.GetRoomItem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;

@Getter
@Setter
public class GetRoomListResponseDto extends ResponseDto {

    private List<GetRoomItem> room;

    public GetRoomListResponseDto(List<GetRoomItem> room){
        super(SUCCESS_CODE, "객실 정보 리스트를 불러왔습니다.");
        this.room=room;
    }

    public static ResponseEntity<GetRoomListResponseDto> success(List<GetRoomItem> room) {
        GetRoomListResponseDto result = new GetRoomListResponseDto(room);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
