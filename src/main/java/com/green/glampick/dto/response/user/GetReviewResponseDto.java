package com.green.glampick.dto.response.user;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.UserReviewListItem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Setter
@Getter

public class GetReviewResponseDto extends ResponseDto {

    Long totalReviewsCount;
    List<UserReviewListItem> reviewListItems;

    private GetReviewResponseDto(Long totalReviewsCount, List<UserReviewListItem> reviewListItems) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.totalReviewsCount = totalReviewsCount;
        this.reviewListItems = reviewListItems;
    }
    public GetReviewResponseDto(List<UserReviewListItem> reviewListItems) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.reviewListItems = reviewListItems;
    }

    public static ResponseEntity<GetReviewResponseDto> success(long totalReviewsCount, List<UserReviewListItem> reviewListItems) {
        GetReviewResponseDto result = new GetReviewResponseDto(totalReviewsCount, reviewListItems);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    public static ResponseEntity<GetReviewResponseDto> success(List<UserReviewListItem> reviewListItems, Long totalReviewsCount) {
        GetReviewResponseDto result = new GetReviewResponseDto(totalReviewsCount,reviewListItems);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
