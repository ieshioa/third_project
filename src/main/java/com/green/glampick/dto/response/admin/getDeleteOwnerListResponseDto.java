package com.green.glampick.dto.response.admin;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.repository.resultset.GetDeleteOwnerListResultSet;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class getDeleteOwnerListResponseDto extends ResponseDto {

    private List<GetDeleteOwnerListResultSet> list;

    private getDeleteOwnerListResponseDto(List<GetDeleteOwnerListResultSet> list) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.list = list;
    }

    public static ResponseEntity<getDeleteOwnerListResponseDto> success(List<GetDeleteOwnerListResultSet> list) {
        getDeleteOwnerListResponseDto result = new getDeleteOwnerListResponseDto(list);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
