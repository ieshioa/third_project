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
public class PostMailSendResponseDto extends ResponseDto {

    private PostMailSendResponseDto() {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    public static ResponseEntity<ResponseDto> success() {
        PostMailSendResponseDto result = new PostMailSendResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}