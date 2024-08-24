package com.green.glampick.dto.request.glamping;

import com.green.glampick.common.GlobalConst;
import com.green.glampick.common.Paging;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GetMoreReviewImageRequestDto extends Paging {
    private long glampId;

    public GetMoreReviewImageRequestDto(Integer page) {
        super(page, GlobalConst.REVIEW_IMAGE_SIZE);
    }

}
