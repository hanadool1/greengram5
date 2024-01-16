package com.green.greengram4.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    // 로그인할 때 얻은 토큰으로 유저정보를 가져온다
    public MyUserDetails getLoginUser() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        return  (MyUserDetails)auth.getPrincipal();
    }

    public int getLoginUserPk() {
        MyUserDetails myUserDetails = getLoginUser();
        return myUserDetails ==null
                ? 0
                : myUserDetails
                .getMyPrincipal()
                .getIuser();
    }

}
