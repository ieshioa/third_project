package com.green.glampick.dto.response.owner.get;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.owner.GetCancelDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetGlampingCancelResponseDto extends ResponseDto {

    List<GetCancelDto> room;
    private String formattedResult;

    private GetGlampingCancelResponseDto(List<GetCancelDto> room, String formattedResult) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.formattedResult = formattedResult;
        this.room = room;
    }

    public static ResponseEntity<GetGlampingCancelResponseDto> success(List<GetCancelDto> room, String formattedResult) {
        GetGlampingCancelResponseDto results = new GetGlampingCancelResponseDto(room, formattedResult);
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }


}
