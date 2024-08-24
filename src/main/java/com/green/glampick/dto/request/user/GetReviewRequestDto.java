package com.green.glampick.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.glampick.common.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.green.glampick.common.GlobalConst.PAGING_SIZE;

@Setter
@Getter
@ToString
public class GetReviewRequestDto extends Paging {
    @Schema(example = "0",description = "전체 -> 0 미답변 -> 1 ")
    private long typeNum;
    @JsonIgnore
    private Long reviewId;
    @JsonIgnore
    private long userId;
    @JsonIgnore
    private Long ownerId;

    @Schema(example = "2024-08-10")
    String firstDate;
    @Schema(example = "2024-08-30")
    String finishDate;

    public GetReviewRequestDto(Integer page) {
        super(page, PAGING_SIZE);
    }
}
