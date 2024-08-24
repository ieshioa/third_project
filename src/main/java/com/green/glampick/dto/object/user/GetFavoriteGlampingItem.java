package com.green.glampick.dto.object.user;

import com.green.glampick.dto.object.GlampingPriceItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFavoriteGlampingItem extends GlampingPriceItem {
//    @Schema(example = "1", description = "글램핑 PK")
//    private long glampId;
    @Schema(example = "뉴욕 카라반", description = "글램핑장의 이름")
    private String glampName;
    @Schema(example = "경북", description = "글램핑장의 위치")
    private String glampLocation;
    @Schema(example = "4.5", description = "평균 별점")
    private double starPoint;
    @Schema(example = "123", description = "리뷰 개수")
    private int reviewCount;
//    @Schema(example = "65,500", description = "글램핑장의 가격")
//    private int price;
    @Schema(example = "vjecie.png", description = "대표 이미지")
    private String glampImage;
}

