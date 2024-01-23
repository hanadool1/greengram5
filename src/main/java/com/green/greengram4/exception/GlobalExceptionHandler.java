package com.green.greengram4.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
// 여러 컨트롤러에 대해 전역적으로 ExceptionHandler를 적용
// 하나의 클래스로 모든 컨트롤러에 대해 전역적으로 예외 처리가 가능함, 직접 정의한 에러 응답을 일관성있게 클라이언트에게 내려줄 수 있음,
// 별도의 try-catch문이 없어 코드의 가독성이 높아짐

public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("handleIllgalArgument", e);
        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER, e.getMessage());
    }

//    @ExceptionHandler(RestApiException.class)//서버 메세지 외에 내가 설정한 메세지를 보내고 싶을 때 사용되는 메소드
//    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
//        log.warn("handleMethodArgumentNotValidException", e);
//
//        /*
//        List<String> errors = new ArrayList<>();
//        for(FieldError lfe : e.getBindingResult().getFieldErrors()){
//            errors.add(lfe.getDefaultMessage());
//        }
//        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER,errors.toString());
//        */
//
//        List<String> errors = e.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(item -> item.getDefaultMessage())
//                .collect(Collectors.toList());
//        // stream 일회용
//
//        return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER,errors.toString());
//    }


    @ExceptionHandler(Exception.class)
    // 대부분의 에러 잡음
    public ResponseEntity<Object> handleException(Exception e) {
        log.warn("handleException", e);
        return handleExceptionInternal(CommonErrorCode.INTERNAL_SERBER_ERROR);
    }

    @ExceptionHandler(RestApiException.class)
    // 커스텀한 에러메세지
    public ResponseEntity<Object> handlerestApiException(RestApiException e) {
        log.warn("handlerestApiException", e);
        return handleExceptionInternal(e.getErrorCode());
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return handleExceptionInternal(errorCode, null);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message){
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(message == null ? makeErrorResponse(errorCode)
                        : makeErrorResponse(errorCode, message));
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return makeErrorResponse(errorCode, errorCode.getMessage());
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .build();
    }
}
