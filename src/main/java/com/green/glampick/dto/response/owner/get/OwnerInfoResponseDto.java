package com.green.glampick.dto.response.owner.get;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.repository.resultset.GetReservationBeforeResultSet;
import com.green.glampick.repository.resultset.GetReservationCancelResultSet;
import com.green.glampick.repository.resultset.GetReservationCompleteResultSet;
import com.green.glampick.repository.resultset.OwnerInfoResultSet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class OwnerInfoResponseDto extends ResponseDto {
    @Schema(example = "green502@gmail.com", description = "회원 이메일")
    public String ownerEmail;
    @Schema(example = "김그린", description = "회원 이름")
    private String ownerName;
    @Schema(example = "1234567890", description = "사업자 번호")
    private String businessNumber;
    @Schema(example = "010-1234-5678", description = "회원 전화번호")
    private String ownerPhone;

    public OwnerInfoResponseDto(OwnerInfoResultSet result){
        super(SUCCESS_CODE, "회원 정보를 불러왔습니다.");
        this.ownerEmail=result.getOwnerEmail();
        this.ownerName=result.getOwnerName();
        this.businessNumber=result.getBusinessNumber();
        this.ownerPhone=result.getOwnerPhone();
    }

    public static ResponseEntity<OwnerInfoResponseDto> success(OwnerInfoResultSet resultSet) {
        OwnerInfoResponseDto result = new OwnerInfoResponseDto(resultSet);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
