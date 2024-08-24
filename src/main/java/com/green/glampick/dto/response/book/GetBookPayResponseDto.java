package com.green.glampick.dto.response.book;

import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetBookPayResponseDto extends ResponseDto {

    private List<Long> roomPrice;  // 객실 가격
    private long extraChargePrice;  // 추가 인원 결제 금액
    private long payAmount;  // 최종 결제가

    private GetBookPayResponseDto(List<Long> roomPrice, long extraChargePrice, long payAmount) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.roomPrice = roomPrice;
        this.extraChargePrice = extraChargePrice;
        this.payAmount = payAmount;
    }

    public static ResponseEntity<GetBookPayResponseDto> success(List<Long> roomPrice, long extraChargePrice, long payAmount) {
        GetBookPayResponseDto result = new GetBookPayResponseDto(roomPrice, extraChargePrice, payAmount);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
