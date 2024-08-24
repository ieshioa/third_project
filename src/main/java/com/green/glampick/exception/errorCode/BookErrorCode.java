package com.green.glampick.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BookErrorCode implements ErrorCode {

    // 400
    DB(HttpStatus.BAD_REQUEST, "중복된 예약입니다."); // DUPLICATED_BOOK

    private final HttpStatus httpStatus;
    private final String message;
}
