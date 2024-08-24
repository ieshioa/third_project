package com.green.glampick.dto.response.owner.get;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


public interface GetOwnerBookCancelCountResponseDto {
    LocalDate getCheckInDate();
    Long getCountCancel();
}
