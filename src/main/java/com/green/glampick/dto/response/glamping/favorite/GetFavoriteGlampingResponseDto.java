package com.green.glampick.dto.response.glamping.favorite;

import com.green.glampick.dto.ResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Setter
@Getter
public class GetFavoriteGlampingResponseDto extends ResponseDto {

    @Schema(example = "01", description = "관심 취소 등록 여부")
    private int resultValue;

    private GetFavoriteGlampingResponseDto(int resultValue) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.resultValue = resultValue;
    }

    public static ResponseEntity<GetFavoriteGlampingResponseDto> success(int resultValue) {
        GetFavoriteGlampingResponseDto result = new GetFavoriteGlampingResponseDto(resultValue);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
