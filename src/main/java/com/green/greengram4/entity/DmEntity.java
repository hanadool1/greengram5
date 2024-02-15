package com.green.greengram4.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_dm")
public class DmEntity extends CreatedAtEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private int idm;

    @Column(name = "last_msg", length = 2000)
    private String lastMsg;

    @LastModifiedDate
    @Column(name = "last_msg_at")
    private LocalDateTime lastMsgAt;
}
