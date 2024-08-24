package com.green.glampick.dto.object.owner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetGlampingInfoItem {

    @Schema(example = "true", description = "글램핑 등록 완료 상태")
    private boolean state;

    @Schema(example = "1")
    private Long glampId;

    @Schema(example = "그린 글램핑")
    private String glampName;

    @Schema(example = "0535721005")
    private String glampCall;

    @Schema(example = "glamping.jpg")
    private String glampImage;

    @Schema(example = "대구광역시 중구 109-2")
    private String glampLocation;

    @Schema(example = "gyeongbuk")
    private String region;

    @Schema(example = "10000", description = "인원 추가 요금")
    private Integer extraCharge;

    @Schema(example = "글램핑 소개")
    private String glampIntro;

    @Schema(example = "기본 정보")
    private String infoBasic;

    @Schema(example = "유의사항")
    private String infoNotice;

    @Schema(example = "추가 주차 정보")
    private String traffic;

    @Schema(example = "0", description = "0이면 심사대기, -1이면 심사반려")
    private Integer exclusionStatus;

}
