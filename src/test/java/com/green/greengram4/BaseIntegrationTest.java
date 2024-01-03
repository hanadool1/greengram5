package com.green.greengram4;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Import(CharEncodingConfig.class)
// @MockMvcConfig (Import랑 둘 중에 한개만 사용하면 됨)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BaseIntegrationTest {
    // Base > 통합테스트할 때 필요한 것들을 모아놓고 상속시켜서 쓰려고 만들어놓음
    @Autowired protected MockMvc mvc;
    // MockMvc : 코드 안(내부)에서 ..웹 프로그램을 실제 서버에 배포하지 않고도 테스트를 위한 요청을 제공하는 수단, 포스트맨 역할
    @Autowired protected ObjectMapper om;
    // objectMapper : 통신할 때 데이터를 주고 받을 때 씀 / 객체를 JSON으로 JSON을 객체로 바꿔주는 기능

}
