package com.green.greengram4.feed;

import com.green.greengram4.common.ResVo;
import com.green.greengram4.feed.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
public class FeedController {
    private final FeedService service;

    @PostMapping
//    public ResVo postFeed(@RequestBody FeedInsDto dto) {
        public FeedPicsInsDto postFeed(@RequestPart(required = false) List<MultipartFile> pics, @RequestPart FeedInsDto dto) {
        log.info("pics : {}", pics.size());
        log.info("dto : {}", dto);
        dto.setPics(pics);
        return service.postFeed(dto);
    }

    @GetMapping
    public List<FeedSelVo> getFeedAll(FeedSelDto dto, @PageableDefault(page = 1, size = 30) Pageable pageable) {
        log.info("dto : {}",dto);
        log.info("pageable : {}", pageable);
        List<FeedSelVo> list = service.getFeedAll(dto, pageable);
        return list;
    }

    @GetMapping("/fav")
    public ResVo toggleFeedFav(FeedFavDto dto){
        return service.toggleFeedFav(dto);
    }

    // ifeed, iuser
    @DeleteMapping
    public ResVo delFeed(FeedDelDto dto) {
        return service.delFeed(dto);
    }

}
