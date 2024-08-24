package com.green.glampick.dto.response.owner.post;

import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class PostRoomInfoResponseDto extends ResponseDto {

    // 테스트용 스웨거에서 보여주기
    private long roomId;

    public PostRoomInfoResponseDto(long roomId) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.roomId = roomId;
    }

    public static ResponseEntity<ResponseDto> success(long roomId) {
        PostRoomInfoResponseDto result = new PostRoomInfoResponseDto(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}