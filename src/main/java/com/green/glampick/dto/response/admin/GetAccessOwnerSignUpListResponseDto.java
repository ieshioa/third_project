package com.green.glampick.dto.response.admin;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.admin.GetAccessOwnerSignUpListItem;
import com.green.glampick.entity.OwnerEntity;
import com.green.glampick.repository.resultset.GetAccessOwnerSignUpListResultSet;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetAccessOwnerSignUpListResponseDto extends ResponseDto {

    private List<GetAccessOwnerSignUpListResultSet> list;

    private GetAccessOwnerSignUpListResponseDto(List<GetAccessOwnerSignUpListResultSet> list) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.list = list;
    }

    public static ResponseEntity<GetAccessOwnerSignUpListResponseDto> success(List<GetAccessOwnerSignUpListResultSet> list) {
        GetAccessOwnerSignUpListResponseDto result = new GetAccessOwnerSignUpListResponseDto(list);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
