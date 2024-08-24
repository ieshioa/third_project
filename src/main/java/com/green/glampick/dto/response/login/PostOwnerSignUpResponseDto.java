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
public class PostOwnerSignUpResponseDto extends ResponseDto {

    private long userId;

    private PostOwnerSignUpResponseDto(long userId) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.userId = userId;
    }

    public static ResponseEntity<PostOwnerSignUpResponseDto> success(long userId) {
        PostOwnerSignUpResponseDto result = new PostOwnerSignUpResponseDto(userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
