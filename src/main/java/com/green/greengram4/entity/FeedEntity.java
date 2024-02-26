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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iuser", nullable = false)
    private UserEntity userEntity;

    @ToString.Exclude // @Data에 @ToString 어노테이션이 있기 때문에, 이 멤버필드는 ToString 제외 시켜달라는 어노테이션
    @OneToMany(mappedBy = "feedEntity", cascade = CascadeType.PERSIST) // 양방향, 영속성 전이
    //영속성전이 이게 빠져있으면 insert할 때 피드, 사진 두번 insert를 해야 하는데
    //내가 직접 날리지 않고 알아서 매핑하여 같이 보내줌
    private List<FeedPicsEntity> feedPicsEntityList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "feedEntity")
    private List<FeedFavEntity> feedFavEntityList = new ArrayList<>();

}
