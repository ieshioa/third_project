package com.green.glampick.dto.response.user;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Setter
@Getter
public class DeleteUserResponseDto extends ResponseDto {

    private long userId;

    private DeleteUserResponseDto(UserEntity userEntity) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.userId = userEntity.getUserId();
    }

    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto(SUCCESS_CODE, SUCCESS_MESSAGE);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
