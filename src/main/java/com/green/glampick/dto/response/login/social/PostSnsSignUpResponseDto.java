package com.green.glampick.dto.response.login.social;

import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class PostSnsSignUpResponseDto extends ResponseDto {

    private Long userId;

    private PostSnsSignUpResponseDto(Long userId) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.userId = userId;
    }

    public static ResponseEntity<PostSnsSignUpResponseDto> success(Long userId) {
        PostSnsSignUpResponseDto result = new PostSnsSignUpResponseDto(userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
