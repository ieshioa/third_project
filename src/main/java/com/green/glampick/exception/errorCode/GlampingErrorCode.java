package com.green.glampick.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlampingErrorCode implements ErrorCode {

    // 400
    NG(HttpStatus.BAD_REQUEST, "존재하지 않는 글램핑입니다."), // NOT_EXISTED_GLAMPING
    WP(HttpStatus.BAD_REQUEST, "인원 정보가 잘못 입력되었습니다."), // WRONG_PERSONNEL
    WD(HttpStatus.BAD_REQUEST, "날짜 정보가 잘못 입력되었습니다."), // WRONG_DATE
    NEGP(HttpStatus.BAD_REQUEST, "성수기 데이터가 존재하지않습니다."); // NOT_EXIST_PEAK

    private final HttpStatus httpStatus;
    private final String message;
}
