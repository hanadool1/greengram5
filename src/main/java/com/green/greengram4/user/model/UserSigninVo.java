package com.green.greengram4.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSigninVo { // 프론트랑 상의해서 받을 정보 입력
    private int result;
    private int iuser;
    private String nm;
    private String pic;
    private String firebaseToken;
    private String accessToken;
}
