package com.green.glampick.dto.response.admin;

import com.green.glampick.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

public class PatchGlampingAccessResponseDto extends ResponseDto {

    private PatchGlampingAccessResponseDto() {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    public static ResponseEntity<PatchGlampingAccessResponseDto> success() {
        PatchGlampingAccessResponseDto result = new PatchGlampingAccessResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
