package com.green.glampick.dto.response.owner.get;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.response.owner.patch.PatchOwnerPeakResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
@ToString
public class GetGlampingPeakPeriodResponseDto extends ResponseDto {
    @Schema(example = "2024-08-15")
    private String startPeakDate;
    @Schema(example = "2024-08-31")
    private String endPeakDate;
    @Schema(example = "50")
    private Integer percent;

    private GetGlampingPeakPeriodResponseDto(String startPeakDate, String endPeakDate,Integer percent) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.startPeakDate = startPeakDate;
        this.endPeakDate = endPeakDate;
        this.percent = percent;
    }

    public static ResponseEntity<GetGlampingPeakPeriodResponseDto> success(String startPeakDate, String endPeakDate,Integer percent) {
        GetGlampingPeakPeriodResponseDto result = new GetGlampingPeakPeriodResponseDto(startPeakDate, endPeakDate, percent);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
