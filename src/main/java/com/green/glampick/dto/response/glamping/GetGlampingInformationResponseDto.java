package com.green.glampick.dto.response.glamping;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.glamping.GlampingDetailReviewItem;
import com.green.glampick.dto.object.glamping.GlampingRoomListItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
@ToString
public class GetGlampingInformationResponseDto extends ResponseDto {

        @Schema(example = "1", description = "글랭핑PK")
        private long glampId;
        @Schema(example = "cb4a0b8f-629e-4d20-9626-cd45347837df.png", description = "글램핑대표사진")
        private String glampImage;
        @Schema(example = "그린 파인트리글램핑&카라반 ", description = "글램핑 명")
        private String glampName;
        @Schema(example = "4.5 ", description = "별점")
        private double starPointAvg;
        @Schema(example = "경기 포천시 이동면 연곡리", description = "주소")
        private String glampLocation;
        @Schema(example = "서울 잠실 40분 거리에 2022년 11월 신축 오픈 캠핑장입니다.", description = "글램핑 소개")
        private String glampIntro;
        @Schema(example = "입실 : 15:00 | 퇴실 : 11:00 (퇴실시간 이후 30분당 오버타임 요금 부과됩니다 퇴실시간을 꼭 지켜주세요)", description = "기본정보")
        private String infoBasic;
        @Schema(example = "주차 가능 (1대 주차 무료 / 1대 추가시 10,000원)", description = "주차장정보")
        private String traffic;
        @Schema(example = "최대 인원 초과시 입실이 불가 합니다 (방문객 불가)", description = "유의사항")
        private String infoNotice;
        @Schema(example = "1329명", description = "리뷰 평가자 숫자")
        private int countReviewUsers;
        @Schema(example = "0 or 1", description = "좋아요 on/off")
        private int isFav;
        @Schema(example = "1 or -1 or 0")
        private int activateStatus;


        private HashSet<String> roomService;
        private List<GlampingDetailReviewItem> reviewItems;
        private List<GlampingRoomListItem> roomItems;




    private GetGlampingInformationResponseDto() {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto(SUCCESS_CODE, SUCCESS_MESSAGE);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
