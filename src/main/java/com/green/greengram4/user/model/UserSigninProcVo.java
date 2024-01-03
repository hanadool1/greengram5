package com.green.greengram4.user.model;

import lombok.Data;

@Data
public class UserSigninProcVo {
    private int iuser;
    private String uid;
    private String upw;
    private String nm;
    private String pic;
    private String createdAt;
    private String updatedAt;
}
