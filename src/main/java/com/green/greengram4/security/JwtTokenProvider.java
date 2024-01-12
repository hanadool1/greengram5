package com.green.greengram4.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.greengram4.common.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Signature;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
// @Component : Bean등록
public class JwtTokenProvider {
    private final ObjectMapper om;
    // Jackson 라이브러리에 있는 것 객체>json, json>객체 왔다갔다할 때 씀
    private final AppProperties appProperties;
    private SecretKeySpec secretKeySpec;

    @PostConstruct
    // 메소드 호출할 때 쓰는 어노테이션, 스프링이 켜질 때 딱 한번
    // 우선 빈 등록이 되어야 함, @PostConstruct 호출하기 전 DI부터 이루어짐
    // 생성자를 통해서 DI를 받지 않더라도, DI가 되고난 후에 빈등록
    public void init() {
        this.secretKeySpec = new SecretKeySpec(appProperties.getJwt().getSecret().getBytes()
                , SignatureAlgorithm.HS256.getJcaName());

    }

    public String generateAccessToken(MyPrincipal principal) {
        return generateToken(principal, appProperties.getJwt().getAccessTokenExpiry());
    }

    public String generateRefreshToken(MyPrincipal principal) {
        return generateToken(principal, appProperties.getJwt().getRefreshTokenExpiry());
    }

    public String generateToken(MyPrincipal principal, long tokenValidMs) {
        Date now = new Date();
        return Jwts.builder()
                .claims(createClaims(principal))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + tokenValidMs))
                .signWith(secretKeySpec)
                .compact();
    }

    private Claims createClaims(MyPrincipal principal) {
        try {
            String json = om.writeValueAsString(principal);
            return Jwts.claims()
                    .add("user", json)
                    .build();
        } catch (Exception e) {
            return null;
        }

    }

    public String resolveToken(HttpServletRequest req) {
        String auth = req.getHeader(appProperties.getJwt().getHeaderSchemeName());
        if (auth == null) { return  null; }
        if (auth.startsWith(appProperties.getJwt().getTokenType())) {
            return auth.substring(appProperties.getJwt().getTokenType().length()).trim();
        }
        return null;
    }



    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKeySpec)
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = getUserDetailsFromToken(token);

        return userDetails == null
                ? null
                : new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public UserDetails getUserDetailsFromToken(String token) {
        try {
            Claims claims = getAllClaims(token);
            String json = (String)claims.get("user");
            MyPrincipal myPrincipal = om.readValue(json, MyPrincipal.class);
            return MyUserDetails.builder()
                    .myPrincipal(myPrincipal)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isValidateToken(String token) {
        try {
            // 만료기간이 현재시간보다 전이면 False, 현재시간이 만료기간보다 후이면 false
            // 만료기간이 현재시간보다 후면 True, 현재시간이 만료기간보다 전이면 true
            return !getAllClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
