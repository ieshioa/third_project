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
public class PostSignUpResponseDto extends ResponseDto {

    private long userId;

    private PostSignUpResponseDto(long userId) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.userId = userId;
    }

    public static ResponseEntity<PostSignUpResponseDto> success(long userId) {
        PostSignUpResponseDto result = new PostSignUpResponseDto(userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}