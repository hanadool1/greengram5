package com.green.greengram4.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSelDto {
    private String uid;
    private int iuser;
    private String prviderType;
}
