package com.green.glampick.dto.response.owner.get;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.owner.GetPopularRoom;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetOwnerPopularRoomResponseDto extends ResponseDto {

    private Long total;
    List<GetPopularRoom> popularRooms;

    private GetOwnerPopularRoomResponseDto(Long total, List<GetPopularRoom> popularRooms) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.popularRooms = popularRooms;
        this.total = total;


    }

    public static ResponseEntity<GetOwnerPopularRoomResponseDto> success(Long total, List<GetPopularRoom> popularRooms) {
        GetOwnerPopularRoomResponseDto result = new GetOwnerPopularRoomResponseDto(total, popularRooms);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
