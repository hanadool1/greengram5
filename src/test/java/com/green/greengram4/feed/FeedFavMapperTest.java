package com.green.greengram4.feed;

import com.green.greengram4.feed.model.FeedDelDto;
import com.green.greengram4.feed.model.FeedFavDto;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MybatisTest // dao들만 객체화시켜서 빈 등록
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 데이터베이스 바꾸지 않고 yaml에 있는 거 사용
class FeedFavMapperTest {

    @Autowired
    private FeedFavMapper mapper;

    @Test
    public void insFeedFav() {
        FeedFavDto dto = new FeedFavDto();
        dto.setIfeed(1);
        dto.setIuser(1);

        List<FeedFavDto> result = mapper.selFeedFavForTest(dto);
        assertEquals(0,result.size(),"첫번째 insert전 미리 확인");

        int affectedRows1 = mapper.insFeedFav(dto);
        assertEquals(1,affectedRows1,"첫번째 insert");

        List<FeedFavDto> result1 = mapper.selFeedFavForTest(dto);
        assertEquals(1,result1.size(),"첫번째 insert 확인");

        dto.setIfeed(11);
        dto.setIuser(2);

        List<FeedFavDto> result2 = mapper.selFeedFavForTest(dto);
        assertEquals(0,result2.size(),"두번째 insert전 미리 확인");

        int affectedRows2 = mapper.insFeedFav(dto);
        assertEquals(1,affectedRows2,"두번째 insert");

        List<FeedFavDto> result3 = mapper.selFeedFavForTest(dto);
        assertEquals(1,result3.size(),"두번째 insert 확인");
    }

    @Test
    public void delFeedFav() {
        FeedFavDto dto = new FeedFavDto();
        dto.setIfeed(3);
        dto.setIuser(4);

        List<FeedFavDto> result = mapper.selFeedFavForTest(dto);
        assertEquals(0,result.size());

        int affctedRow1 = mapper.delFeedFav(dto);
        assertEquals(1,affctedRow1);

        int affectedRow2 = mapper.delFeedFav(dto);
        assertEquals(0, affectedRow2);

        List<FeedFavDto> result2 = mapper.selFeedFavForTest(dto);
        assertEquals(0,result2.size());
    }

//    @Test
//    public void delFeedByFav() {
//        final int ifeed = 3;
//
//        FeedFavDto selDto = new FeedFavDto();
//        selDto.setIfeed(ifeed);
//        List<FeedFavDto> selList = mapper.selFeedFavForTest(selDto);
//
//        FeedDelDto dto = new FeedDelDto();
//        dto.setIfeed(ifeed);
//        int delAffectedRows = mapper.delFeedByFav(dto);
//        assertEquals(selList.size(),delAffectedRows);
//
//        List<FeedFavDto> selList2 = mapper.selFeedFavForTest(selDto);
//        assertEquals(0,selList2.size());
//
//    }
}