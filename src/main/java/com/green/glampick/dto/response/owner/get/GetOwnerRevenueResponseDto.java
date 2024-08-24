package com.green.glampick.dto.response.owner.get;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.owner.GetRevenue;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;


@Getter
@Setter
public class GetOwnerRevenueResponseDto extends ResponseDto {

    private Long totalPay;
   List<GetRevenue> revenue;

    private GetOwnerRevenueResponseDto(Long totalPay, List<GetRevenue> revenue) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.totalPay = totalPay;
        this.revenue = revenue;

    }

    public static ResponseEntity<GetOwnerRevenueResponseDto> success(Long totalPay, List<GetRevenue> revenue) {
        GetOwnerRevenueResponseDto result = new GetOwnerRevenueResponseDto(totalPay, revenue);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
