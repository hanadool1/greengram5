package com.green.greengram4.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
// getter가 있어서 interface인 ErrorCode를 오버라이딩 하지않아도 빨간줄 안뜸

//@RequiredArgsConstructor
// final 들어간 멤버필드 생성자 만들어줌
public enum AuthErrorCode implements ErrorCode{
    // 상수 객체화

    VALID_EXIST_USER_ID(HttpStatus.NOT_FOUND,"아이디가 존재하지 않습니다."),
    VALID_PASSWORD(HttpStatus.NOT_FOUND,"비밀번호를 확인해주세요."),
    NEED_SIGNIN(HttpStatus.UNAUTHORIZED,"로그인이 필요합니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "refresh-token이 없습니다.");
    // static final 멤버필드

    private final HttpStatus httpStatus;
    // 내부에서만 생성하기 때문에 private 사용
    private final String message;

    AuthErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
