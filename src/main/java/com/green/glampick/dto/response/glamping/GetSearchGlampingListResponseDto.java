package com.green.glampick.dto.response.glamping;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.glamping.GlampingListItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetSearchGlampingListResponseDto extends ResponseDto {

    @Schema(example = "123", description = "검색된 글램핑 개수")
    private int searchCount;
    private List<GlampingListItem> glampingListItems;

    private GetSearchGlampingListResponseDto(int searchCount, List<GlampingListItem> glampingListItems) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.searchCount = searchCount;
        this.glampingListItems = glampingListItems;
    }

    public static ResponseEntity<GetSearchGlampingListResponseDto> success(int searchCount, List<GlampingListItem> glampingListItems) {
        GetSearchGlampingListResponseDto result = new GetSearchGlampingListResponseDto(searchCount, glampingListItems);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> isNull() {
        ResponseDto result = new ResponseDto("RN", "검색 결과가 없습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }



}
