package com.green.glampick.dto.response.glamping;

import com.green.glampick.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Slf4j
@Getter
@Setter
@SuperBuilder
@ToString
public class GetMoreReviewImgageResponseDto extends ResponseDto {

    private List<String> moreReviewImage;

    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto(SUCCESS_CODE, SUCCESS_MESSAGE);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public GetMoreReviewImgageResponseDto(List<String> moreReviewImage) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.moreReviewImage = moreReviewImage;
    }
}

