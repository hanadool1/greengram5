package com.green.greengram4.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "t_feed")
public class FeedEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long ifeed;

    @Column(length = 1000)
    private String contents;

    @Column(length = 100)
    private String location;

    @ManyToOne
    @JoinColumn(name = "iuser", nullable = false)
    private UserEntity userEntity;

    @ToString.Exclude // @Data에 @ToString 어노테이션이 있기 때문에, 이 멤버필드는 ToString 제외 시켜달라는 어노테이션
    @OneToMany(mappedBy = "feedEntity", cascade = CascadeType.PERSIST) // 양방향
    private List<FeedPicsEntity> feedPicsEntityList = new ArrayList<>();

}
