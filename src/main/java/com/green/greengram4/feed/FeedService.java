package com.green.greengram4.feed;

import com.green.greengram4.common.Const;
import com.green.greengram4.common.MyFileUtils;
import com.green.greengram4.common.ResVo;
import com.green.greengram4.feed.model.*;
import com.green.greengram4.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class FeedService {
    private final FeedMapper mapper;
    private final FeedPicsMapper picsMapper;
    private final FeedFavMapper favMapper;
    private final FeedCommentMapper commentMapper;
    private final AuthenticationFacade authenticationFacade;
    private final MyFileUtils myFileUtils;

    public FeedPicsInsDto postFeed(FeedInsDto dto) {
        dto.setIuser(authenticationFacade.getLoginUserPk());
        // authenticationFacade에서 로그인한 유저 pk를 가져와서 dto에 넣는다
        int affectedFeed = mapper.insFeed(dto);
        // mapper 실행
        String target = "/feed/" + dto.getIfeed();
        // user 폴더를 만들고 폴더이름을 피드의 pk로 설정
        FeedPicsInsDto pDto = new FeedPicsInsDto();
        // 객체화
        pDto.setIfeed(dto.getIfeed());
        // pDto의 ifeed에 dto의 ifeed를 넣는다
        for (MultipartFile file : dto.getPics()) {
            // 반복문
            String saveFileNm = myFileUtils.transferTo(file,target);
            // 메모리에 있는 내용을 파일로 옮기는 작업
            pDto.getPics().add(saveFileNm);
            // pDto의 사진들에 saveFileNm을 넣는다
        }
        int feedPicsAffectedRow = picsMapper.insFeedPics(pDto);
        // (피드에 사진넣는)mapper 실행
        return pDto;

    }

    public List<FeedSelVo> getFeedAll(FeedSelDto dto) {
        // FeedSelDto에 rowCount는 20 고정, startIdx는 메소드를 통해 미리 계산
        List<FeedSelVo> feedList = mapper.selFeedAll(dto);

        FeedCommentSelDto fcDto = new FeedCommentSelDto();
        // FeedCommentSelDto 객체화
        fcDto.setStartIdx(0);
        // fcDto에 있는 startIdx에 0을 (setter를 사용하여) 넣는다
        fcDto.setRowCount(4);
        // fcDto에 있는 rowCount에 4를 (setter를 사용하여) 넣는다

        for (FeedSelVo vo : feedList) {
            // feedList 반복
            List<String> picList = picsMapper.selFeedPicsAll(vo.getIfeed());
            // selFeedPicsAll 실행(한 페이지에 있는 피드들의 pk값을 파라미터로 가져온다)
            vo.setPics(picList);
            // vo에 있는 pics에 picList를 (setter를 이용해서) 넣는다

            fcDto.setIfeed(vo.getIfeed());
            // 위에서 fcDto에 startIdx와 rowCount를 넣었고,
            // 반복문 안에서 vo의 ifeed를 (getter를 이용하여) 빼서
            // fcDto에 있는 ifeed에 (setter를 이용하여) 넣는다
            List<FeedCommentSelVo> commentList = commentMapper.selFeedCommentAll(fcDto);
            // selFeedCommentAll 실행(반복문 돌면서 commentList에 댓글 저장)
            if (commentList.size() == Const.FEED_COMMENT_FIRST_CNT) {
                // commentList의 사이즈가 == 4인 경우
                vo.setIsMoreComment(1);
                // 댓글 더보기
                commentList.remove(commentList.size()-1);
                // commentList에 저장된 마지막 댓글 한개 지우기
            }
            vo.setComments(commentList);
            // vo에 있는 comments에 commentList를 (setter를 이용해서) 넣는다
        }
        return feedList;
    }

    public ResVo toggleFeedFav(FeedFavDto dto){
        // ResVo - result 값은 삭제 했을 시 (좋아요 취소) 0
        // 등록했을 시 (좋아요 실행) 1
        int togglefav = favMapper.delFeedFav(dto);
        // 우선 좋아요 취소
        if (togglefav == 0) {
            // 영향받은 행이 0이라면
            favMapper.insFeedFav(dto);
            // 좋아요 실행
            return new ResVo(Const.FEED_FAV_ADD);
            // 1 리턴
        }
        return new ResVo(Const.FEED_FAV_DEL);
        // delFeedFav가 실행되었을 경우 좋아요 취소상태이므로 0리턴
    }


    public ResVo delFeed(FeedDelDto dto) {
        // 피드삭제
        int check = mapper.selFeed(dto);
        // 피드 삭제전 피드가 있는 지 확인
        if (check > 0) {
            // check가 양수일 경우 피드가 있다는 뜻
            picsMapper.delFeedByPics(dto);
            // 피드 사진 삭제
            favMapper.delFeedByFav(dto);
            // 피드 좋아요 삭제
            commentMapper.delFeedByComment(dto);
            // 피드 댓글 삭제
            mapper.delFeed(dto);
            // 피드 삭제
            return new ResVo(Const.SUCCESS);
            // 1 리턴
        }
        return new ResVo(Const.FAIL);
        // 삭제한 피드가 없을 때 0 리턴
    }

}
