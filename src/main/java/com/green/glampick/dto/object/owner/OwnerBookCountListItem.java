package com.green.glampick.dto.object.owner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OwnerBookCountListItem {
    @Schema(example = "2024-08-26")
    private String checkInDate;
    @Schema(example = "5")
    private Long ingCount;
    @Schema(example = "1")
    private Long cancelCount;
    @Schema(example = "3")
    private Long completeCount;

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate.toString();
    }
}
