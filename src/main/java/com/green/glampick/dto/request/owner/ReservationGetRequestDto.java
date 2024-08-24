package com.green.glampick.dto.request.owner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.glampick.common.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.green.glampick.common.GlobalConst.PAGING_SIZE;
@Getter
@Setter
@ToString
public class ReservationGetRequestDto extends Paging {
    @JsonIgnore
    @Schema(example = "1", description = "사장님PK")
    private Long ownerId;

    @Schema(example = "2024-08-07", description = "캘린더 데이트")
    private String date;

    public ReservationGetRequestDto(Integer page) {
        super(page, PAGING_SIZE);
    }
}
