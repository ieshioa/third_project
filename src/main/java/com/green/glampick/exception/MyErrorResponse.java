package com.green.glampick.exception;

import com.green.glampick.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@SuperBuilder
public class MyErrorResponse extends ResponseDto {

    private final HttpStatus statusCode;
    private final List<ValidationError> valids;

    //    Validation Error 가 발생 시 해당하는 에러 메시지를 표시할 때 사용하는 객체
    //    inner class는 static을 붙여주면 성능이 좋아진다.
    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {
        // validation 에러 메시지
        private final String message;

        // ValidationError 객체 생성을 담당하는 메소드
        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}