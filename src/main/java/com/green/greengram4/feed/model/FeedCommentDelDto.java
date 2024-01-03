package com.green.greengram4.feed.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedCommentDelDto {
    private int ifeedComment;
    private int iuser;
}
