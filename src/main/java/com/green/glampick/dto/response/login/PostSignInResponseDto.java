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
public class PostSignInResponseDto extends ResponseDto {

    private String accessToken;

    private PostSignInResponseDto(String accessToken) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.accessToken = accessToken;
    }

    public static ResponseEntity<PostSignInResponseDto> success(String accessToken) {
        PostSignInResponseDto result = new PostSignInResponseDto(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}