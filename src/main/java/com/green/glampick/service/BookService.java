package com.green.glampick.service;

import com.green.glampick.dto.request.book.GetBookPayRequestDto;
import com.green.glampick.dto.request.book.PostBookRequestDto;
import com.green.glampick.dto.response.book.GetBookPayResponseDto;
import com.green.glampick.dto.response.book.PostBookResponseDto;
import org.springframework.http.ResponseEntity;

public interface BookService {

    //  글램핑 예약하기  //
    ResponseEntity<? super PostBookResponseDto> postBook(PostBookRequestDto dto);

    ResponseEntity<? super GetBookPayResponseDto> getReservationAmount(GetBookPayRequestDto dto);
}
