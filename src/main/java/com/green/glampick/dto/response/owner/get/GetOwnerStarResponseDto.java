package com.green.glampick.dto.response.owner.get;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.owner.GetStarHeart;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetOwnerStarResponseDto extends ResponseDto {

    List<GetStarHeart> point;

    private GetOwnerStarResponseDto(List<GetStarHeart> point) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.point = point;


    }

    public static ResponseEntity<GetOwnerStarResponseDto> success(List<GetStarHeart> point) {
        GetOwnerStarResponseDto result = new GetOwnerStarResponseDto(point);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
