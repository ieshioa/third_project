package com.green.glampick.dto.request.owner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ReviewGetRevenueRequestDto {

    @JsonIgnore
    private long ownerId;
    @Schema(example = "2024-07-01")
    private LocalDate startDayId;
    @Schema(example = "2024-07-31")
    private LocalDate endDayId;
}
