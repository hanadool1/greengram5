package com.green.greengram4.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_dm_msg")
public class DmMsgEntity extends CreatedAtEntity{

    @EmbeddedId
    private DmMsgIds dmMsgIds;
    // 복합키 걸기

    @ManyToOne
    @MapsId("idm")
    @JoinColumn(name = "idm", columnDefinition = "BIGINT UNSIGNED")
    private DmEntity dmEntity;
    // 복합키 건 idm 외래키 걸기

    @ManyToOne
    @JoinColumn(name = "iuser", nullable = false)
    private UserEntity userEntity;
    // 외래키만 걸기

    @Column(length = 2000, nullable = false)
    private String msg;


}
