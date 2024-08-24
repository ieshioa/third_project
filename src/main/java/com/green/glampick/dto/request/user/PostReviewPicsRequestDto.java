package com.green.glampick.dto.request.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
public class PostReviewPicsRequestDto {

    private long reviewId;

    @Builder.Default
    private List<String> reviewPicsName = new ArrayList<>();

}