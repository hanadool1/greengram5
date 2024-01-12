package com.green.greengram4.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    public MyUserDetails getLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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
