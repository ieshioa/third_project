package com.green.glampick.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CancelBookRequestDto {

    @JsonIgnore private long userId;
    private long reservationId;
    private String comment;

}
