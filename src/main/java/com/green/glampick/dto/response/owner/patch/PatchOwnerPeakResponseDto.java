package com.green.glampick.dto.response.owner.patch;

import com.green.glampick.dto.ResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

public class PatchOwnerPeakResponseDto extends ResponseDto {
    @Schema(example = "0 or 1", description = "수정실패 0 수정성공 1")
    private int result;

    private PatchOwnerPeakResponseDto() {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        result = 1;
    }

    public static ResponseEntity<PatchOwnerPeakResponseDto> success() {
        PatchOwnerPeakResponseDto result = new PatchOwnerPeakResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
