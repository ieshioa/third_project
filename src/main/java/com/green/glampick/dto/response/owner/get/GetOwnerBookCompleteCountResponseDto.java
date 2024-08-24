package com.green.glampick.dto.response.owner.get;


import java.time.LocalDate;

public interface GetOwnerBookCompleteCountResponseDto {
    LocalDate getCheckInDate();
    Long getCountComplete();


}
