package com.green.glampick.dto.object;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlampingPriceItem {
    @Schema(example = "1", description = "글램핑 PK")
    private long glampId;

    @Schema(example = "65,500", description = "글램핑장의 가격")
    private int price;
}
