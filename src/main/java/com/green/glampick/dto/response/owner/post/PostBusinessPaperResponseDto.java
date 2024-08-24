package com.green.glampick.dto.response.owner.post;

import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class PostBusinessPaperResponseDto extends ResponseDto {

    private PostBusinessPaperResponseDto() {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    public static ResponseEntity<PostBusinessPaperResponseDto> success() {
        PostBusinessPaperResponseDto result = new PostBusinessPaperResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
