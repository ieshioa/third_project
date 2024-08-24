package com.green.glampick.dto.request.owner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PatchOwnerPeakRequestDto {

    @Schema(example = "2024-08-08", description = "성수기 시작날짜")
    @NotBlank(message = "peakStartDay 값이 입력되지않았습니다.")
    private String peakStartDay;

    @Schema(example = "2024-08-31", description = "성수기 끝나는 날짜")
    @NotBlank(message = "peakEndDay 값이 입력되지않았습니다.")
    private String peakEndDay;

    @Schema(example = "25", description = "가격인상 퍼센트")
    @NotNull(message = "peakCost 값이 입력되지않았습니다")
    private Integer peakCost;

    @JsonIgnore
    private Long ownerId;
}
