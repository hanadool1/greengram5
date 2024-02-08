package com.green.greengram4.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.Security;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 세션의 특징 : 메모리 차지가 크다
        return httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(http -> http.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers
                                        ("/api/feed"
                                        ,"/api/feed/comment"
                                        ,"/api/dm"
                                        ,"/api/dm/msg"
                                ).authenticated()
                                .requestMatchers
                                        (HttpMethod.POST,"/api/user/signout"
                                        ,"/api/user/follow"
                                ).authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/user").authenticated()
                                .requestMatchers(HttpMethod.PATCH, "/api/user/pic").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/feed/fav").hasAnyRole("ADMIN")
                                .anyRequest().permitAll()
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(except -> {
                    except.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                            .accessDeniedHandler(new JwtAccessDeniedHandler());
                })
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // PasswordEncoder 추상클래스를 implements한 BCryptPasswordEncoder
    }

}
