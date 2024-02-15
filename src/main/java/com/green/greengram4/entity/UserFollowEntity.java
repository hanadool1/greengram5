package com.green.greengram4.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_user_follow")
public class UserFollowEntity extends CreatedAtEntity{
    @EmbeddedId
    private UserFollowIds userFollowIds;
    // 복합키 거는 거

    @ManyToOne
    @MapsId("fromIuser")
    @JoinColumn(name = "from_iuser", columnDefinition = "BIGINT UNSIGNED")
    private UserEntity fromUserEntity;
    // 외래키 거는 거

    @ManyToOne
    @MapsId("toIuser")
    @JoinColumn(name = "to_iuser", columnDefinition = "BIGINT UNSIGNED")
    private UserEntity toUserEntity;

}
