package com.green.glampick.dto.response.user;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.entity.ReviewEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class DeleteReviewResponseDto extends ResponseDto {

    private Long reviewId;

    private DeleteReviewResponseDto(ReviewEntity reviewEntity) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.reviewId = reviewEntity.getReviewId();
    }

    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto(SUCCESS_CODE, SUCCESS_MESSAGE);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
