package com.green.greengram4.user;

import com.green.greengram4.common.*;
import com.green.greengram4.entity.UserEntity;
import com.green.greengram4.entity.UserFollowEntity;
import com.green.greengram4.entity.UserFollowIds;
import com.green.greengram4.exception.RestApiException;
import com.green.greengram4.security.AuthenticationFacade;
import com.green.greengram4.security.JwtTokenProvider;
import com.green.greengram4.security.MyPrincipal;
import com.green.greengram4.security.MyUserDetails;
import com.green.greengram4.user.model.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.green.greengram4.exception.AuthErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final UserRepository repository;
    private final UserFollowRepositoty followRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AppProperties appProperties;
    private final CookieUtils cookieUtils;
    private final AuthenticationFacade authenticationFacade;
    private final MyFileUtils myFileUtils;




//    public ResVo signup(UserSignupDto dto) {
////        String hashedPw = BCrypt.hashpw(dto.getUpw(), salt);
//        //비밀번호 암호화
//        String hashedPw = passwordEncoder.encode(dto.getUpw());
//
//        UserSignupProcDto pDto = new UserSignupProcDto();
//        pDto.setUid(dto.getUid());
//        pDto.setUpw(hashedPw);
//        pDto.setNm(dto.getNm());
//        pDto.setPic(dto.getPic());
//
//        log.info("before - pDto.iuser : {}", pDto.getIuser());
//        int affectedRows = mapper.insUser(pDto);
//        log.info("after - pDto.iuser : {}", pDto.getIuser());
//
//        return new ResVo(pDto.getIuser()); //회원가입한 iuser pk값이 리턴
//    }

    public ResVo signup(UserSignupDto dto) {
        String hashedPw = passwordEncoder.encode(dto.getUpw());

        UserEntity entity = UserEntity.builder()
                .providerType(ProviderTypeEnum.LOCAL)
                .uid(dto.getUid())
                .upw(hashedPw)
                .nm(dto.getNm())
                .pic(dto.getPic())
                .role(RoleEnum.USER)
                .build();
        repository.save(entity);

        return new ResVo(entity.getIuser().intValue());
        // entity의 pk가 long타입이기 때문에 intValue 사용하여 컨버팅
    }

//    public UserSigninVo signin(HttpServletResponse res, UserSigninDto dto) {
//        // 아이디, 비번 인증처리
//        UserSelDto sDto = new UserSelDto();
//        sDto.setUid(dto.getUid());
//
//        UserModel entity = mapper.selUser(sDto);
//        if(entity == null) { // 아이디 없음
//            throw new RestApiException(VALID_EXIST_USER_ID);
//            //} else if(!BCrypt.checkpw(dto.getUpw(), entity.getUpw())) {
//        } else if (!passwordEncoder.matches(dto.getUpw(),entity.getUpw())) {
//            throw new RestApiException(VALID_PASSWORD);
//        }
//        // AT, RT 발행
//        MyPrincipal myPrincipal = MyPrincipal.builder()
//                .iuser(entity.getIuser())
//                .build();
//
//        myPrincipal.getRoles().add(entity.getRole());
//
//        String at = jwtTokenProvider.generateAccessToken(myPrincipal);
//        String rt = jwtTokenProvider.generateRefreshToken(myPrincipal);
//
//        int rtCookieMaxAge = (int)appProperties.getJwt().getRefreshTokenCookieMaxAge();
//        cookieUtils.deleteCookie(res, "rt");
//        cookieUtils.setCookie(res, "rt", rt, rtCookieMaxAge);
//
//        return UserSigninVo.builder()
//                .result(Const.SUCCESS)
//                .iuser(entity.getIuser())
//                .nm(entity.getNm())
//                .pic(entity.getPic())
//                .firebaseToken(entity.getFirebaseToken())
//                .accessToken(at)
//                // Response Body에 AT를 담아서 리턴, RT Cookie에 담는다.
//                .build();
//    }

    public UserSigninVo signin(HttpServletResponse res, UserSigninDto dto) {
        Optional<UserEntity> optEntity = repository.findByProviderTypeAndUid(ProviderTypeEnum.LOCAL, dto.getUid());
        UserEntity entity = optEntity.orElseThrow(() -> new RestApiException(VALID_EXIST_USER_ID));
        // 아이디 없음 경우

        if (!passwordEncoder.matches(dto.getUpw(), entity.getUpw())) {
            throw new RestApiException(VALID_PASSWORD);
        }
        // 비밀번호 틀림

        // AT, RT 발행
        int iuser = entity.getIuser().intValue();
        MyPrincipal myPrincipal = MyPrincipal.builder()
                .iuser(iuser)
                .build();

        myPrincipal.getRoles().add(entity.getRole().name());

        String at = jwtTokenProvider.generateAccessToken(myPrincipal);
        String rt = jwtTokenProvider.generateRefreshToken(myPrincipal);

        int rtCookieMaxAge = (int)appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(res, "rt");
        cookieUtils.setCookie(res, "rt", rt, rtCookieMaxAge);

        return UserSigninVo.builder()
                .result(Const.SUCCESS)
                .iuser(iuser)
                .nm(entity.getNm())
                .pic(entity.getPic())
                .firebaseToken(entity.getFirebaseToken())
                .accessToken(at)
                // Response Body에 AT를 담아서 리턴, RT Cookie에 담는다.
                .build();
    }

    public ResVo signout(HttpServletResponse res) {
        cookieUtils.deleteCookie(res, "rt");
        return new ResVo(1);
    }

    public UserInfoVo getUserInfo(UserInfoSelDto dto) {
        return mapper.selUserInfo(dto);
    }

    public UserSigninVo getRefreshToken(HttpServletRequest req) {
        // at 갱신 > at 시간이 짧다 
        // Cookie cookie = cookieUtils.getCookie(req, "rt");
        Optional<String> optRt = cookieUtils.getCookie(req,"rt").map(Cookie::getValue);
        if (optRt.isEmpty()) {
            return UserSigninVo.builder()
                    .result(Const.FAIL)
                    .accessToken(null)
                    .build();
        }
        String token = optRt.get();
        if (!jwtTokenProvider.isValidateToken(token)) {
            return UserSigninVo.builder()
                    .result(Const.FAIL)
                    .accessToken(null)
                    .build();
        }

        MyUserDetails myUserDetails = (MyUserDetails) jwtTokenProvider.getUserDetailsFromToken(token);
        MyPrincipal myPrincipal = myUserDetails.getMyPrincipal();

        String at = jwtTokenProvider.generateAccessToken(myPrincipal);

        return UserSigninVo.builder()
                .result(Const.SUCCESS)
                .accessToken(at)
                .build();
    }

//    public ResVo patchUserFirebaseToken(UserFirebaseTokenPatchDto dto) {
//        int affectedRows = mapper.updUserFirebaseToken(dto);
//        return new ResVo(affectedRows);
//    }

    @Transactional
    public ResVo patchUserFirebaseToken(UserFirebaseTokenPatchDto dto) {
        UserEntity entity = repository.getReferenceById((long)authenticationFacade.getLoginUserPk());
        entity.setFirebaseToken(dto.getFirebaseToken());
        return new ResVo(Const.SUCCESS);
    }

//    public UserPicPatchDto patchUserPic(MultipartFile pic) {
//        UserPicPatchDto dto = new UserPicPatchDto();
//        dto.setIuser(authenticationFacade.getLoginUserPk());
//        // authenticationFacade에서 로그인한 유저 pk를 가져와서 dto에 넣는다
//        String path = "/user/" + dto.getIuser();
//        // user 폴더를 만들고 폴더이름을 유저의 pk로 설정
//
//        myFileUtils.delFolderTrigger(path);
//
//        String file = myFileUtils.transferTo(pic,path);
//        // 메모리에 있는 내용을 파일로 옮기는 작업
//        dto.setPic(file);
//        // dto의 pic에 file을 넣는다
//        int affectedRows = mapper.updUserPic(dto);
//        // mapper 실행
//        return dto;
//    }

    @Transactional
    public UserPicPatchDto patchUserPic(MultipartFile pic) {
        Long iuser = (long)authenticationFacade.getLoginUserPk();
        UserEntity entity = repository.getReferenceById(iuser);
        String path = "/user/" + iuser;
        myFileUtils.delFolderTrigger(path);
        String savedPicFileNm = myFileUtils.transferTo(pic, path);
        entity.setPic(savedPicFileNm);

        UserPicPatchDto dto = new UserPicPatchDto();
        dto.setIuser(iuser.intValue());
        dto.setPic(savedPicFileNm);

        return dto;
    }

//    public ResVo toggleFollow(UserFollowDto dto) {
//        int delAffectedRows = mapper.delUserFollow(dto);
//        if(delAffectedRows == 1) {
//            return new ResVo(Const.FAIL);
//        }
//        int insAffectedRows = mapper.insUserFollow(dto);
//        return new ResVo(Const.SUCCESS);
//    }

    @Transactional
    public ResVo toggleFollow(UserFollowDto dto) {
        UserFollowIds ids = new UserFollowIds();
        ids.setFromIuser((long)authenticationFacade.getLoginUserPk());
        ids.setToIuser(dto.getToIuser());

        AtomicInteger atomic = new AtomicInteger(Const.FAIL);
                followRepository.findById(ids)
                .ifPresentOrElse( entity -> followRepository.delete(entity), () -> {
                            atomic.set(Const.SUCCESS);
                            UserFollowEntity saveUserFollowEntity = new UserFollowEntity();
                            saveUserFollowEntity.setUserFollowIds(ids);
                            UserEntity fromUserEntity = repository.getReferenceById((long)authenticationFacade.getLoginUserPk());
                            UserEntity toUserEntity = repository.getReferenceById(dto.getToIuser());
                            saveUserFollowEntity.setFromUserEntity(fromUserEntity);
                            saveUserFollowEntity.setToUserEntity(toUserEntity);
                            followRepository.save(saveUserFollowEntity);
                        }
                );
        return new ResVo(atomic.get());
    }
}