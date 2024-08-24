package com.green.glampick.dto.request.owner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GlampingPostRequestDto {
    // glamping 테이블
    @JsonIgnore
    private long ownerId;

    @NotBlank(message = "글램핑 이름이 입력되지 않았습니다.")
    @Size(max = 50, message = "글램핑 이름은 최대 50자 입력 가능합니다.")
    @Schema(example = "뉴욕 카라반", description = "글램핑 이름")
    private String glampName;

    @Size(min = 8, max = 14, message = "전화번호 입력이 잘못되었습니다.")
    @Schema(example = "0535721005", description = "글램핑 전화번호")
    private String glampCall;

    @NotBlank(message = "글램핑 주소가 입력되지 않았습니다.")
    @Size(max = 50, message = "글램핑 주소는 최대 50자 입력 가능합니다.")
    @Schema(example = "대구광역시 중구 109-2", description = "글램핑 주소")
    private String glampLocation;

    @NotBlank(message = "지역 분류가 입력되지 않았습니다.")
    @Size(min = 4, max = 9, message = "지역은 정해진 분류 내에서 입력해주세요.")
    @Schema(example = "서울경기", description = "지역 분류")
    private String region;

    @Schema(example = "10000", description = "추가 인원에 대한 추가 요금")
    private Integer extraCharge;

    @NotBlank(message = "글램핑 소개가 입력되지 않았습니다.")
    @Schema(example = "소개소개", description = "글램핑 소개")
    private String intro;

    @NotBlank(message = "기본 정보가 입력되지 않았습니다.")
    @Schema(example = "글램핑입니다", description = "기본 정보")
    private String basic;

    @NotBlank(message = "이용 안내가 입력되지 않았습니다.")
    @Schema(example = "이거 저거 주의해주세요", description = "이용 안내")
    private String notice;

    @NotBlank(message = "추가 위치 정보가 입력되지 않았습니다.")
    @Schema(example = "해수욕장 10분", description = "추가 위치 정보")
    private String traffic;
//
//    @Schema(example = "위도")
//    private Double lat;
//
//    @Schema(example = "경도")
//    private Double lng;

    // 이미지
    @JsonIgnore
    private String glampingImg;
    // 이미지 업로드를 위한 pk
    @JsonIgnore
    private long glampId;

}
