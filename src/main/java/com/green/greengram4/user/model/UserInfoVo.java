package com.green.greengram4.user.model;

import lombok.Data;

@Data
public class UserInfoVo {
    private int feedCnt; //등록한 피드 수
    private int favCnt; //등록한 피드에 달린 좋아요 수
    private String nm;
    private String createdAt;
    private String pic;
    private int follower;
    private int following;
    private int followState;
}