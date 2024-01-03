package com.green.greengram4.feed;

import com.green.greengram4.common.ResVo;
import com.green.greengram4.feed.model.FeedCommentDelDto;
import com.green.greengram4.feed.model.FeedCommentInsDto;
import com.green.greengram4.feed.model.FeedCommentSelDto;
import com.green.greengram4.feed.model.FeedCommentSelVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FeedCommentService {
    private final FeedCommentMapper mapper;

    public ResVo postFeedComment(FeedCommentInsDto dto) {
        int comment = mapper.insFeedComment(dto);
        return new ResVo(dto.getIfeedComment());
    }

    public List<FeedCommentSelVo> getFeedCommentAll(FeedCommentSelDto dto) {
        return mapper.selFeedCommentAll(dto);
    }

    public ResVo delFeedComment(FeedCommentDelDto dto){
        return new ResVo(mapper.delFeedComment(dto));
    }

}
