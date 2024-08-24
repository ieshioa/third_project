package com.green.glampick.dto.response.owner.put;

import com.green.glampick.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

public class PatchOwnerInfoResponseDto extends ResponseDto{
    private PatchOwnerInfoResponseDto() {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    // 변경사항 없음
    public static ResponseEntity<ResponseDto> noUpdate() {
        ResponseDto result = new ResponseDto(SUCCESS_CODE, "변경된 내용이 없습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 변경 완료
    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto(SUCCESS_CODE, "변경이 완료되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
