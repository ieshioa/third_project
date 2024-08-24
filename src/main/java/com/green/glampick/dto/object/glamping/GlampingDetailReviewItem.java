package com.green.glampick.dto.object.glamping;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GlampingDetailReviewItem {
    @Schema(example = "박보영귀여워", description = "리뷰 작성자")
    private String userNickName;
    @Schema(example = "진짜 너무너무 좋은 숙소였어요~!", description = "리뷰 내용")
    private String content;
}
