package com.green.glampick.dto.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReviewListItem {

    @JsonIgnore
    private long reviewId;

    @Schema(example = "d7c01900-24d2-4a9d-a86f-f7057173a0cb.jpeg", description = "유저프로필이미지")
    private String userProfileImage;

    @Schema(example = "누텔라범벅와플", description = "유저 닉네임")
    private String userNickName;

    @Schema(example = "4.6", description = "별점")
    private int starPoint;

    @Schema(example = "2024-07-02", description = "리뷰작성날짜")
    private String createdAt;

    @Schema(example = "아잇 너무 좋았어요!", description = "유저가작성한리뷰내용")
    private String userReviewContent;

    @Schema(example = "너가 좋다니 나도좋아!", description = "사장이작성한리뷰내용")
    private String ownerReviewContent;

    @Schema(example = "카바란 1호", description = "리뷰한 객실명")
    private String roomName;

    private List<String> reviewImages;
}
