package com.green.glampick.dto.object;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserReviewListItem {

    @Schema(example = "글램핑 장소 이름", description = "글램핑 장소 이름")
    private String glampName;

    @Schema(example = "방 이름", description = "방 이름")
    private String roomName;

    @Schema(example = "누텔라범벅와플", description = "유저 닉네임")
    private String userNickName;

    @Schema(example = "d7c01900-24d2-4a9d-a86f-f7057173a0cb.jpeg", description = "유저 프로필 이미지")
    private String userProfileImage;

    @Schema(example = "1", description = "리뷰 PK")
    private long reviewId;

    @Schema(example = "1", description = "예약 PK")
    private long reservationId;

    @Schema(example = "아잇 너무 좋았어요!", description = "유저가 작성한 리뷰 내용")
    private String userReviewContent;

    @Schema(example = "4.6", description = "별점")
    private int starPoint;

    @Schema(example = "너가 좋다니 나도좋아!", description = "사장이 작성한 리뷰 내용")
    private String ownerReviewContent;

    @Schema(example = "2024-07-02", description = "리뷰 작성 날짜")
    private String createdAt;

    @Schema(example = "2024070820095", description = "예약 번호")
    private long bookId;

    @Schema(example = "[\"image1.jpg\", \"image2.jpg\"]", description = "리뷰 이미지 목록")
    private List<String> reviewImages;

    private long glampId;
}
