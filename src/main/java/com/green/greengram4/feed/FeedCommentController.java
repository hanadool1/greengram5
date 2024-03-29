package com.green.greengram4.feed;

import com.green.greengram4.common.ResVo;
import com.green.greengram4.exception.FeedErrorCode;
import com.green.greengram4.exception.RestApiException;
import com.green.greengram4.feed.model.FeedCommentDelDto;
import com.green.greengram4.feed.model.FeedCommentInsDto;
import com.green.greengram4.feed.model.FeedCommentSelDto;
import com.green.greengram4.feed.model.FeedCommentSelVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/feed/comment")
public class FeedCommentController {
    private final FeedCommentService service;

    @PostMapping
    public ResVo postFeedComment(@Valid @RequestBody FeedCommentInsDto dto){

        log.info("dto : {}",dto);
        return service.postFeedComment(dto);
    }

    @GetMapping
    public List<FeedCommentSelVo> getFeedCommentAll(int ifeed) {
        // 4~999까지의 레코드만 리턴
        FeedCommentSelDto dto = new FeedCommentSelDto();
        dto.setIfeed(ifeed);
        dto.setStartIdx(3);
        dto.setRowCount(999);
        return service.getFeedCommentAll(dto);
    }

    @DeleteMapping
    private ResVo delFeedComment(@RequestParam("ifeed_comment") int ifeedComment,
                                 @RequestParam("logined_iuser") int loginedIuser) {
        return service.delFeedComment(FeedCommentDelDto.builder()
                        .ifeedComment(ifeedComment)
                        .iuser(loginedIuser)
                        .build());
    }


}
