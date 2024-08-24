package com.green.glampick.dto.response.user;

import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class PostReviewResponseDto extends ResponseDto {

    private long reviewId;

    private PostReviewResponseDto(long reviewId) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.reviewId = reviewId;
    }

    public static ResponseEntity<PostReviewResponseDto> success(long reviewId) {
        PostReviewResponseDto result = new PostReviewResponseDto(reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }



}
