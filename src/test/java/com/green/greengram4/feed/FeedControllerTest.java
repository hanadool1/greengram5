package com.green.greengram4.feed;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.green.greengram4.MockMvcConfig;
import com.green.greengram4.CharEncodingConfig;
import com.green.greengram4.common.ResVo;
import com.green.greengram4.feed.model.FeedInsDto;
import com.green.greengram4.feed.model.FeedSelVo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@MockMvcConfig
@WebMvcTest(FeedController.class)
@Import(CharEncodingConfig.class)
class FeedControllerTest {

    @Autowired
    private MockMvc mvc;
    // post로 보낼지 delete, get, patch로 보낼지 선택해주는 기능
    @MockBean
    private FeedService service;
    // FeedController가 메모리에 올라감(가짜주소값)
    @Autowired
    private ObjectMapper mapper;
//
//    @Test
//    void postFeed()throws Exception{
//        ResVo result = new ResVo(2);
//        //when(service.postFeed(any())).thenReturn(result);
//        given(service.postFeed(any())).willReturn(result);// when이랑 같은 역할 뭐가들어오든 result를 리턴해라
//
//        FeedInsDto dto = new FeedInsDto();
//
//        mvc.perform( //포스트맨에서 send보내는거랑 비슷함 perform메소드 안에 파라미터 type은 MockMvcrequestBuilder
//                        MockMvcRequestBuilders
//                                .post("/api/feed")
//                                .contentType(MediaType.APPLICATION_JSON) //MediaType을  JSON으로 setting Body부분에 있음
//                                .content(mapper.writeValueAsString(dto))
//
//                )
//                .andExpect(status().isOk()) //isOk가 200으로 리턴됐는지 확인 // { "result": 5 }
//                .andExpect(content().string(mapper.writeValueAsString(result)))
//                .andDo(print()); // 결과를 프린트
//
//        verify(service).postFeed(any()); // 호출됐는지 확인 메소드 (호출만 된다면 true)
//
//    }

    @SneakyThrows
    @Test
    void selFeedAll() throws Exception{
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");
        params.add("loginedIuser", "4");

        List<String> pics = new ArrayList<>();
        pics.add("pic!!");
        pics.add("good!!");
        List<String> pics2 = new ArrayList<>();
        pics2.add("Yeah!!");
        pics2.add("Hi!!");
        FeedSelVo vo = new FeedSelVo();
        vo.setIfeed(1);
        vo.setContents("하이");
        vo.setLocation("ddddddd");
        vo.setWriterIuser(3);
        vo.setPics(pics);

        FeedSelVo vo2 = new FeedSelVo();
        vo2.setIfeed(2);
        vo2.setContents("gg");
        vo2.setLocation("ooo");
        vo2.setWriterIuser(4);
        vo2.setPics(pics2);

        List<FeedSelVo> list = new ArrayList<>();
        list.add(vo);
        list.add(vo2);
        given(service.getFeedAll(any())).willReturn(list);


        mvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/feed")
                                .params(params)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(list)))
                .andDo(print());

        verify(service).getFeedAll(any());
    }

    @Test
    void toggleFeedFav() {
    }

    @Test
    void delFeed() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("ifeed", "1");
        params.add("iuser", "2");
        ResVo result = new ResVo(1);

        mvc.perform(
                        MockMvcRequestBuilders.delete("/api/feed")
                                .params(params)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(result)))
                .andDo(print());


        verify(service).delFeed(any());
    }
}