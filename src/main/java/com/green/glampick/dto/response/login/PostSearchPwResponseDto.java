package com.green.glampick.dto.response.login;

import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class PostSearchPwResponseDto extends ResponseDto {

    private PostSearchPwResponseDto() {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    public static ResponseEntity<PostSearchPwResponseDto> success() {
        PostSearchPwResponseDto result = new PostSearchPwResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
