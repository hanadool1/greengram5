package com.green.greengram4.feed;

import com.green.greengram4.entity.FeedCommentEntity;
import com.green.greengram4.entity.FeedEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedCommentRepository extends JpaRepository<FeedCommentEntity, Long> {
    @EntityGraph(attributePaths = {"userEntity"})
    List<FeedCommentEntity> findAllTop4ByFeedEntity(FeedEntity feedEntity);
    // findAll:select, Top4:limit, FeedEntity:where절
}
