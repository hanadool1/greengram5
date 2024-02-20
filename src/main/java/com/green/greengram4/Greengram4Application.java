package com.green.greengram4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

import java.awt.print.Pageable;

@EnableJpaAuditing
@ConfigurationPropertiesScan
@SpringBootApplication
public class Greengram4Application {

    public static void main(String[] args) {
        SpringApplication.run(Greengram4Application.class, args);
    }

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customizer() {
        return p -> p.setOneIndexedParameters(true);
    }
    // 페이지가 1부터 시작하게 하는 인터페이스
    // 추상메서드가 오직 하나인 함수형 인터페이스 -> 자바의 람다식 사용 가능
}
