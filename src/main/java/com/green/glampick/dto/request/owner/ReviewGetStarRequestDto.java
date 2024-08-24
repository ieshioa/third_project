package com.green.glampick.dto.request.owner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.glampick.entity.GlampingEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewGetStarRequestDto {
    @JsonIgnore
    private long userId;
    @JsonIgnore
    private long ownerId;

}
