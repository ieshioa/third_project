package com.green.glampick.dto.response.owner;

import com.green.glampick.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

public class OwnerSuccessResponseDto extends ResponseDto {

    private OwnerSuccessResponseDto() {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
    }


    public static ResponseEntity<ResponseDto> postInformation() {
        ResponseDto result = new ResponseDto(SUCCESS_CODE, "등록을 완료하였습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> updateInformation() {
        ResponseDto result = new ResponseDto(SUCCESS_CODE, "수정을 완료하였습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> deleteInformation() {
        ResponseDto result = new ResponseDto(SUCCESS_CODE, "삭제를 완료하였습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> passwordTrue() {
        ResponseDto result = new ResponseDto(SUCCESS_CODE, "비밀번호가 일치합니다.");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> withdraw() {
        ResponseDto result = new ResponseDto(SUCCESS_CODE, "탈퇴 승인을 요청하였습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
