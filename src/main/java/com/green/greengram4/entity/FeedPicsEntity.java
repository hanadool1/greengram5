package com.green.greengram4.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "t_feed_pics")
public class FeedPicsEntity extends CreatedAtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long ifeedPics;

    @ManyToOne
    @JoinColumn(name = "ifeed", nullable = false)
    private FeedEntity feedEntity;

    @Column(length = 2100,nullable = false)
    private String pic;

}
