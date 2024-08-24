package com.green.glampick.dto.request.glamping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GetFavoriteRequestDto {
    @JsonIgnore
    private long userId;
    @Schema(example = "5", description = "글랭핑PK")
    private long glampId;
}
