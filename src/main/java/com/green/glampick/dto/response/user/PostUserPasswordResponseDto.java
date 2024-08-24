package com.green.glampick.dto.response.user;

import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class PostUserPasswordResponseDto extends ResponseDto {

    private boolean checkPw;

    private PostUserPasswordResponseDto(boolean checkPw) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.checkPw = checkPw;
    }

    public static ResponseEntity<PostUserPasswordResponseDto> success() {
        PostUserPasswordResponseDto result = new PostUserPasswordResponseDto(true);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
