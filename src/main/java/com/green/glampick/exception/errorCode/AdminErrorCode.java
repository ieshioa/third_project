package com.green.glampick.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminErrorCode implements ErrorCode{

    // 400
    NWO(HttpStatus.BAD_REQUEST, "탈퇴 신청한 사장이 아닙니다."),
    NFI(HttpStatus.BAD_REQUEST, "사업자 등록증 이미지 정보를 찾을 수 없습니다."),
    NFB(HttpStatus.BAD_REQUEST, "배너를 찾을 수 없습니다."),
    NG(HttpStatus.BAD_REQUEST, "글램핑 정보를 불러오지 못하였습니다."),
    UFF(HttpStatus.BAD_REQUEST, "저장가능한 이미지 수를 초과하였습니다."); // DUPLICATED_BOOK

    private final HttpStatus httpStatus;
    private final String message;

}
