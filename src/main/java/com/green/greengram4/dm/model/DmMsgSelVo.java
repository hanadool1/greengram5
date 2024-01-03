package com.green.greengram4.dm.model;

import lombok.Data;

@Data
public class DmMsgSelVo {
    private int seq;
    private String msg;
    private String createdAt;
    private int writerIuser;
    private String writerPic;
}
