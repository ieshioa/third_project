package com.green.glampick.dto.response.login.mail;

import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class PostMailCheckResponseDto extends ResponseDto {

    private boolean authCheck;

    private PostMailCheckResponseDto(boolean authCheck) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.authCheck = authCheck;
    }

    private PostMailCheckResponseDto(String code, String message, boolean authCheck) {
        super(code, message);
        this.authCheck = authCheck;
    }

    public static ResponseEntity<PostMailCheckResponseDto> success() {
        PostMailCheckResponseDto result = new PostMailCheckResponseDto(true);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}