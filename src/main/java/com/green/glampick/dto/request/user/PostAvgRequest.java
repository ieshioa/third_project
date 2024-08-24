package com.green.glampick.dto.request.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostAvgRequest {

    private long userId;
    private long glampId;
    private long starPointAvg;
    private long reviewCount;
}
