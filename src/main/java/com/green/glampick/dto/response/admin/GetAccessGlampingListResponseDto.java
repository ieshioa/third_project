package com.green.glampick.dto.response.admin;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.repository.resultset.GetAccessGlampingListResultSet;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetAccessGlampingListResponseDto extends ResponseDto {

    List<GetAccessGlampingListResultSet> list;

    private GetAccessGlampingListResponseDto(List<GetAccessGlampingListResultSet> list) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.list = list;
    }

    public static ResponseEntity<GetAccessGlampingListResponseDto> success(List<GetAccessGlampingListResultSet> list) {
        GetAccessGlampingListResponseDto result = new GetAccessGlampingListResponseDto(list);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
