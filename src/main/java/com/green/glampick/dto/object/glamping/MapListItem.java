package com.green.glampick.dto.object.glamping;

import com.green.glampick.dto.object.GlampingPriceItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MapListItem extends GlampingPriceItem {

    @Schema(example = "뉴욕카라반", description = "글램핑장의 이름")
    private String glampName;

    @Schema(example = "aof5eqx.png", description = "글램핑장의 대표 이미지")
    private String glampPic;

    @Schema(example = "4.6", description = "글램핑장의 별점")
    private Double starPoint;

    @Schema(example = "123", description = "글램핑장의 리뷰 개수")
    private int reviewCount;

//    @Schema(example = "위도")
//    private Double lat;
//
//    @Schema(example = "위도")
//    private Double lng;

    private String location;

}
