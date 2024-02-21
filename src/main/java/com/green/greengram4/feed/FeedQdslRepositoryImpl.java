package com.green.greengram4.feed;

import com.green.greengram4.entity.FeedEntity;
import com.green.greengram4.feed.model.FeedSelVo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.stream.Collectors;

import static com.green.greengram4.entity.QFeedEntity.feedEntity;

@Slf4j
@RequiredArgsConstructor
public class FeedQdslRepositoryImpl implements FeedQdslRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<FeedSelVo> selFeedAll(long loginIuser, long targetIuser, Pageable pageable) {
        List<FeedEntity> list = jpaQueryFactory.select(feedEntity)
                .from(feedEntity)
                .where(whereTargetIuser(targetIuser))
                .orderBy(feedEntity.ifeed.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return list.stream().map(feed ->
                FeedSelVo.builder()
                .ifeed(feed.getIfeed().intValue())
                .contents(feed.getContents())
                .location(feed.getLocation())
                .createdAt(feed.getCreatedAt().toString())
                .writerIuser(feed.getUserEntity().getIuser().intValue())
                .writerNm(feed.getUserEntity().getNm())
                .writerPic(feed.getUserEntity().getPic())
                .pics(feed.getFeedPicsEntityList().stream().map(pic ->
                        pic.getPic()).collect(Collectors.toList()))
                .isFav(feed.getFeedFavEntityList().stream().anyMatch(fav ->
                        fav.getUserEntity().getIuser() > loginIuser) ? 1 : 0)
                .build())
                .collect(Collectors.toList());
    }

    private BooleanExpression whereTargetIuser(long targetIuser) {
        return targetIuser == 0 ? null : feedEntity.userEntity.iuser.eq(targetIuser);
    }
}
