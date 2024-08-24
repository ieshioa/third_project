package com.green.glampick.dto.response.login.sms;

import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class PostSmsCheckResponseDto extends ResponseDto {

    private boolean phoneCheck;

    private PostSmsCheckResponseDto(boolean phoneCheck) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.phoneCheck = phoneCheck;
    }

    public static ResponseEntity<PostSmsCheckResponseDto> success() {
        PostSmsCheckResponseDto result = new PostSmsCheckResponseDto(true);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
