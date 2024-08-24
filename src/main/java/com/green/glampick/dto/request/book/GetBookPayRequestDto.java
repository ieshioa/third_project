package com.green.glampick.dto.request.book;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GetBookPayRequestDto {

    private long roomId;

    private long personnel;

    private long glampId;

    LocalDate checkInDate;
    LocalDate checkOutDate;

}
