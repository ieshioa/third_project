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
public class PostSearchEmailResponseDto extends ResponseDto {

    private String userEmail;

    private PostSearchEmailResponseDto(String userEmail) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.userEmail = userEmail;
    }

    public static ResponseEntity<PostSearchEmailResponseDto> success(String userEmail) {
        PostSearchEmailResponseDto result = new PostSearchEmailResponseDto(userEmail);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
