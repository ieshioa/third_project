package com.green.glampick.dto.response.glamping;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.glamping.GlampingListItem;
import com.green.glampick.dto.object.glamping.MapListItem;
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
public class GetSearchMapListResponseDto extends ResponseDto {

    private List<MapListItem> mapList;

    private GetSearchMapListResponseDto(List<MapListItem> mapList) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.mapList = mapList;
    }

    public static ResponseEntity<GetSearchMapListResponseDto> success(List<MapListItem> mapList) {
        GetSearchMapListResponseDto result = new GetSearchMapListResponseDto(mapList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
