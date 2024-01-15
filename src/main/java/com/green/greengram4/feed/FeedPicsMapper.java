package com.green.greengram4.feed;

import com.green.greengram4.feed.model.FeedDelDto;
import com.green.greengram4.feed.model.FeedInsDto;
import com.green.greengram4.feed.model.FeedPicsInsDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedPicsMapper {
    int insFeedPics(FeedPicsInsDto dto);
    List<String> selFeedPicsAll(int ifeed);
    int delFeedByPics(FeedDelDto dto);
}
