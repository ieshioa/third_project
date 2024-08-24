package com.green.glampick.dto.response.owner.get;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.owner.OwnerBookCountListItem;
import com.green.glampick.dto.object.owner.OwnerBookDetailListItem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetOwnerBookListResponseDto extends ResponseDto {

    private List<OwnerBookDetailListItem> before;
    private List<OwnerBookDetailListItem> complete;
//    private List<OwnerBookDetailListItem> cancel;

    private List<OwnerBookCountListItem> countList;

    public GetOwnerBookListResponseDto(List<OwnerBookDetailListItem> before,List<OwnerBookDetailListItem> complete) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.before = before;
        this.complete = complete;
    }

    public GetOwnerBookListResponseDto(List<OwnerBookCountListItem> countList) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.countList = countList;
    }
    public ResponseEntity<ResponseDto> success(List<OwnerBookDetailListItem> before, List<OwnerBookDetailListItem> complete) {
        GetOwnerBookListResponseDto result = new GetOwnerBookListResponseDto(before,complete);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    public ResponseEntity<ResponseDto> success(List<OwnerBookCountListItem> countList) {
        GetOwnerBookListResponseDto result = new GetOwnerBookListResponseDto(countList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
