package com.green.greengram4.feed;

import com.green.greengram4.entity.FeedEntity;
import com.green.greengram4.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<FeedEntity, Long>, FeedQdslRepository {
    @EntityGraph(attributePaths = {"userEntity"})
    // EntityGraph는 JPA가 어떤 엔티티를 불러올 때 이 엔티티와 관계된 엔티티를 불러올 것인지에 대한 정보를 제공한다.
    // n+1 해결
    List<FeedEntity> findAllByUserEntityOrderByIfeedDesc(UserEntity userEntity, Pageable pageable);
}
