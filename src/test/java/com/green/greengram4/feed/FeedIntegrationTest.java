package com.green.greengram4.feed;


import com.green.greengram4.BaseIntegrationTest;
import com.green.greengram4.common.ResVo;
import com.green.greengram4.feed.model.FeedInsDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeedIntegrationTest extends BaseIntegrationTest {

    @Test
    @Rollback(false) // 실제 데이터 베이스에 저장되지 않도록 false
    public void postFeed() throws Exception{
        FeedInsDto dto = new FeedInsDto();
        dto.setIuser(3);
        dto.setContents("통합 테스트 작업 중");
        dto.setLocation("그린컴퓨터학원");
        List<String> pics = new ArrayList<>();
        pics.add("https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzEyMDJfMjc0%2FMDAxNzAxNTIzMTA3Mzk3.tYgZjInAwzSKnKBEswf_qcDf8WaXHUo4sEzKUk9zqNgg._VGQEJBrk67Oq76SiPT1j6S4VR3IYlkWqDY1iDg03Wwg.JPEG.hyunaa98%2FIMG_8784.JPG&type=sc960_832");
        pics.add("https://i.namu.wiki/i/tcjtuVfCgbGkYdb_z1r03tQwDNPQwliB_4zOb3Z8P2cSmWx7u8i5euwz9aEMX-MxEHugOlgWkrIwJLTNAv5i_w.webp");
        dto.setPics(pics);

        String json = om.writeValueAsString(dto);
        System.out.println("json :" + json);

        MvcResult mr = mvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/feed")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String content = mr.getResponse().getContentAsString();
        // { result : 1 }
        ResVo vo = om.readValue(content, ResVo.class); // String to JSON 문자열을 객체로
        assertEquals(true, vo.getResult() > 0);
    }

    @Test
    @Rollback(false)
    public void delFeed() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("ifeed", "125");
//        params.add("iuser", "3");

        MvcResult mr = mvc.perform(
                        MockMvcRequestBuilders.delete("/api/feed?ifeed=124&iuser=3")
                                //.params(params)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String content = mr.getResponse().getContentAsString();
        ResVo vo = om.readValue(content, ResVo.class);
        assertEquals(1, vo.getResult());
    }


}
