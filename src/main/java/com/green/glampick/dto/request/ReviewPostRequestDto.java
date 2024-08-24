package com.green.glampick.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewPostRequestDto {
    @NotNull
    @Schema(example = "10", description = "글램핑PK")
    private long glampId;

    @NotNull
    @Schema(example = "14", description = "리뷰PK")
    private long reviewId;

    @JsonIgnore
    private long ownerId;

    @Size(max = 500 , message = "내용은 500자 이상 입력 할 수 없습니다.")
    @Schema(example = "이용해 주셔서 감사합니다.", description = "사장 코멘트")
    private String reviewOwnerContent;

}
