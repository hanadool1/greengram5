package com.green.greengram4.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        // Request에 Authorization 키값이 있는지 확인 > 없으면 그냥 통과
        //>있지만 기간만료가 되었는지 확인 만료되었으면 통과

        log.info("JwtAuthentication-Token: {}", token);

        if (token != null && jwtTokenProvider.isValidateToken(token)) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)jwtTokenProvider.getAuthentication(token);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        //>있으면 Token값을 빼내서,
        // Authentication 객체를 만들어서
        // SecurityContextHolder 안의
        // SecurityContext 객체 안의
        // Authentication 객체 주소값을 담는다
        filterChain.doFilter(request, response);

    }
}
