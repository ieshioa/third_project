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
public class PostAdminSignInResponseDto extends ResponseDto {

    private String accessToken;

    private PostAdminSignInResponseDto(String accessToken) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.accessToken = accessToken;
    }

    public static ResponseEntity<PostAdminSignInResponseDto> success(String accessToken) {
        PostAdminSignInResponseDto result = new PostAdminSignInResponseDto(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
