package com.green.greengram4.user;

import com.green.greengram4.common.AppProperties;
import com.green.greengram4.common.Const;
import com.green.greengram4.common.CookieUtils;
import com.green.greengram4.common.ResVo;
import com.green.greengram4.security.JwtTokenProvider;
import com.green.greengram4.security.MyPrincipal;
import com.green.greengram4.security.MyUserDetails;
import com.green.greengram4.user.model.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AppProperties appProperties;
    private final CookieUtils cookieUtils;

    public ResVo signup(UserSignupDto dto) {
//        String hashedPw = BCrypt.hashpw(dto.getUpw(), salt);
        //비밀번호 암호화
        String hashedPw = passwordEncoder.encode(dto.getUpw());

        UserSignupProcDto pDto = new UserSignupProcDto();
        pDto.setUid(dto.getUid());
        pDto.setUpw(hashedPw);
        pDto.setNm(dto.getNm());
        pDto.setPic(dto.getPic());

        log.info("before - pDto.iuser : {}", pDto.getIuser());
        int affectedRows = mapper.insUser(pDto);
        log.info("after - pDto.iuser : {}", pDto.getIuser());

        return new ResVo(pDto.getIuser()); //회원가입한 iuser pk값이 리턴
    }

    public UserSigninVo signin(HttpServletRequest req, HttpServletResponse res, UserSigninDto dto) {
        // 아이디, 비번 인증처리
        UserSelDto sDto = new UserSelDto();
        sDto.setUid(dto.getUid());

        UserEntity entity = mapper.selUser(sDto);
        if(entity == null) {
            return UserSigninVo.builder().result(Const.LOGIN_NO_UID).build();
            //} else if(!BCrypt.checkpw(dto.getUpw(), entity.getUpw())) {
        } else if (!passwordEncoder.matches(dto.getUpw(),entity.getUpw())) {
            return UserSigninVo.builder().result(Const.LOGIN_DIFF_UPW).build();
        }
        // AT, RT 발행
        MyPrincipal myPrincipal = MyPrincipal.builder()
                .iuser(entity.getIuser())
                .build();

        String at = jwtTokenProvider.generateAccessToken(myPrincipal);
        String rt = jwtTokenProvider.generateRefreshToken(myPrincipal);

        int rtCookieMaxAge = (int)appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(res, "rt");
        cookieUtils.setCookie(res, "rt", rt, rtCookieMaxAge);

        return UserSigninVo.builder()
                .result(Const.SUCCESS)
                .iuser(entity.getIuser())
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
        Cookie cookie = cookieUtils.getCookie(req, "rt");
        if (cookie == null) {
            return UserSigninVo.builder()
                    .result(Const.FAIL)
                    .accessToken(null)
                    .build();
        }
        String token = cookie.getValue();
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

    public ResVo patchUserFirebaseToken(UserFirebaseTokenPatchDto dto) {
        int affectedRows = mapper.updUserFirebaseToken(dto);
        return new ResVo(affectedRows);
    }

    public ResVo patchUserPic(UserPicPatchDto dto) {
        int affectedRows = mapper.updUserPic(dto);
        return new ResVo(affectedRows);
    }

    public ResVo toggleFollow(UserFollowDto dto) {
        int delAffectedRows = mapper.delUserFollow(dto);
        if(delAffectedRows == 1) {
            return new ResVo(Const.FAIL);
        }
        int insAffectedRows = mapper.insUserFollow(dto);
        return new ResVo(Const.SUCCESS);
    }
}