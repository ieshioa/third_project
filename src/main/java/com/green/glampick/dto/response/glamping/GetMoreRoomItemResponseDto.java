package com.green.glampick.dto.response.glamping;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.glamping.GlampingRoomListItem;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
@ToString
@SuperBuilder
public class GetMoreRoomItemResponseDto extends ResponseDto {
    private List<GlampingRoomListItem> roomItems;

    private GetMoreRoomItemResponseDto() {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
    }
    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto(SUCCESS_CODE, SUCCESS_MESSAGE);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public GetMoreRoomItemResponseDto(List<GlampingRoomListItem> roomItems) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.roomItems = roomItems;
    }
}
