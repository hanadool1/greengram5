package com.green.greengram4.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FeedInsDto {
    @JsonIgnore
    //@Schema(hidden = true)
    private Long ifeed;
    @JsonIgnore
    private Long iuser;
    private String contents;
    private String location;
    @JsonIgnore
    private List<MultipartFile> pics;
}
