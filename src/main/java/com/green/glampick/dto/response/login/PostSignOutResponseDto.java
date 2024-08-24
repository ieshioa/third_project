package com.green.glampick.dto.response.login;

import com.green.glampick.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

public class PostSignOutResponseDto extends ResponseDto {

    private PostSignOutResponseDto() {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
