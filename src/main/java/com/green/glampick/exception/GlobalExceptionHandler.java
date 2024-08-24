package com.green.glampick.exception;

import com.green.glampick.exception.errorCode.CommonErrorCode;
import com.green.glampick.exception.errorCode.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 커스텀한 예외가 발생되었을 경우 캐치
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        log.error("CustomException - handlerException : {}", e.getMessage());
        return handleExceptionInternal(e.getErrorCode());
    }

    // validation 어노테이션에서 걸린 예외 발생
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER, ex);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return handleExceptionInternal(errorCode, null);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, BindException e) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, e));
    }

    private MyErrorResponse makeErrorResponse(ErrorCode errorCode, BindException e) {
        return MyErrorResponse.builder()
                .statusCode(errorCode.getHttpStatus())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .valids(e == null
                        ? null
                        // validation 에러 메시지를 정리
                        : getValidationError(e)
                )
                .build();
    }

    private List<MyErrorResponse.ValidationError> getValidationError(BindException e) {
        List<MyErrorResponse.ValidationError> list = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            list.add(MyErrorResponse.ValidationError.of(fieldError));
        }
        return list;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityExceptions(Exception e) {
        log.error("Exception - handlerException : {}", e.getMessage());
        e.printStackTrace();
        return handleExceptionInternal(CommonErrorCode.INVALID_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptions(Exception e) {
        log.error("Exception - handlerException : {}", e.getMessage());
        e.printStackTrace();
        return handleExceptionInternal(CommonErrorCode.DBE);
    }
}
