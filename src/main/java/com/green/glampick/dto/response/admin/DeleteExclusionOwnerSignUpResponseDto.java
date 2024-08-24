package com.green.glampick.dto.response.admin;

import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class DeleteExclusionOwnerSignUpResponseDto extends ResponseDto {

    private DeleteExclusionOwnerSignUpResponseDto() {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    public static ResponseEntity<DeleteExclusionOwnerSignUpResponseDto> success() {
        DeleteExclusionOwnerSignUpResponseDto result = new DeleteExclusionOwnerSignUpResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
