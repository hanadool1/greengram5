package com.green.greengram4.feed;

import com.green.greengram4.entity.FeedEntity;
import com.green.greengram4.entity.FeedPicsEntity;
import com.green.greengram4.feed.model.FeedSelDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;


import java.util.List;

import static com.green.greengram4.entity.QFeedEntity.feedEntity;
import static com.green.greengram4.entity.QFeedFavEntity.feedFavEntity;
import static com.green.greengram4.entity.QFeedPicsEntity.feedPicsEntity;

@Slf4j
@RequiredArgsConstructor
public class FeedQdslRepositoryImpl implements FeedQdslRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<FeedEntity> selFeedAll(FeedSelDto dto, Pageable pageable) {
        JPAQuery<FeedEntity> jpaQuery = jpaQueryFactory.select(feedEntity)
                .where(whereTargetIuser(dto.getTargetIuser()))
                //(whereTargetUser(targetIuser),whereTargetUser(targetIuser)) 쉼표로 and조건 사용가능
                .from(feedEntity)
                .orderBy(feedEntity.ifeed.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if(dto.getIsFavList() == 1) {
            jpaQuery.join(feedFavEntity)
                    .on(feedEntity.ifeed.eq(feedFavEntity.feedEntity.ifeed)
                    , feedFavEntity.userEntity.iuser.eq(dto.getLoginedIuser()));
        } else  {
            jpaQuery.where(whereTargetIuser(dto.getTargetIuser()));
        }

        return jpaQuery.fetch();


//        return list.stream().map(feed ->
//                FeedSelVo.builder()
//                .ifeed(feed.getIfeed().intValue())
//                .contents(feed.getContents())
//                .location(feed.getLocation())
//                .createdAt(feed.getCreatedAt().toString())
//                .writerIuser(feed.getUserEntity().getIuser().intValue())
//                .writerNm(feed.getUserEntity().getNm())
//                .writerPic(feed.getUserEntity().getPic())
//                .pics(feed.getFeedPicsEntityList().stream().map(pic ->
//                        pic.getPic()).collect(Collectors.toList()))
//                .isFav(feed.getFeedFavEntityList().stream().anyMatch(fav ->
//                        fav.getUserEntity().getIuser() > dto.getLoginedIuser()) ? 1 : 0)
//                .build())
//                .collect(Collectors.toList());
    }

    @Override
    public List<FeedPicsEntity> selFeedPicsAll(List<FeedEntity> feedEntityList) {
        return jpaQueryFactory.select(Projections.fields(FeedPicsEntity.class,feedPicsEntity.feedEntity,feedPicsEntity.pic))
                .from(feedPicsEntity)
                .where(feedPicsEntity.feedEntity.in(feedEntityList))
                .fetch();
    }

    private BooleanExpression whereTargetIuser(long targetIuser) {
        return targetIuser == 0 ? null : feedEntity.userEntity.iuser.eq(targetIuser);
    }
}
