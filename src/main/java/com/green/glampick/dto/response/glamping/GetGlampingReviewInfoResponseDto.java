package com.green.glampick.dto.response.glamping;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.ReviewListItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
@SuperBuilder
public class GetGlampingReviewInfoResponseDto extends ResponseDto {
    private List<ReviewListItem> reviewListItems;
    private List<String> roomNames;
    private List<String> allReviewImage;
    private GetGlampingReviewInfoResponseDto() {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto(SUCCESS_CODE, SUCCESS_MESSAGE);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public GetGlampingReviewInfoResponseDto(List<ReviewListItem> reviewListItems, List<String> roomNames, List<String> allReviewImage) {

        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.reviewListItems = reviewListItems;
        this.roomNames = roomNames;
        this.allReviewImage = allReviewImage;

    }

}
