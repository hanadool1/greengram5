package com.green.greengram4.dm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class DmInsDto {
    @JsonIgnore
    private int idm; // auto_increment값 가져오려고

    private int loginedIuser;
    private int otherPersonIuser;
}
