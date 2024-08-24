package com.green.glampick.dto.response.glamping;

import com.green.glampick.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Setter
@Getter
@SuperBuilder
@ToString
public class GetMoreRoomImageResponseDto extends ResponseDto {

    private HashMap<String, List<String>> moreRoomImages;

    public GetMoreRoomImageResponseDto(HashMap<String, List<String>> moreRoomImages) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.moreRoomImages = moreRoomImages;
    }

    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto(SUCCESS_CODE, SUCCESS_MESSAGE);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
