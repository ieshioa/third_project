package com.green.glampick.exception;

import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// RuntimeException + ErrorCode를 implements한 객체 주소값을 담을 수 있는 기능

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(CommonErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
