package com.green.glampick.controller.controllerInterface;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.green.glampick.CharEncodingConfiguration;
import com.green.glampick.common.security.AppProperties;
import com.green.glampick.controller.OwnerControllerImpl;
import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.request.owner.GlampingPostRequestDto;
import com.green.glampick.dto.response.owner.OwnerSuccessResponseDto;
import com.green.glampick.jwt.JwtTokenProvider;
import com.green.glampick.security.AuthenticationFacade;
import com.green.glampick.service.OwnerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.mock.web.MockMultipartFile;

@Import({ CharEncodingConfiguration.class })
//@WebMvcTest({ OwnerControllerImpl.class })
@WebMvcTest(controllers = OwnerControllerImpl.class, excludeAutoConfiguration = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ExtendWith(MockitoExtension.class)
//@SpringBootTest
class OwnerControllerTest {


    @Autowired private ObjectMapper om;
    @Autowired private MockMvc mvc;
    @MockBean private OwnerService service;
    @MockBean private AuthenticationFacade authenticationFacade;
    @MockBean private JwtTokenProvider jwtTokenProvider;
    @MockBean private AppProperties appProperties;
    @Autowired
    private WebApplicationContext wac;
    private final String BASE_URL = "/api/owner";

    @BeforeEach
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    void createGlamping()  throws Exception {
        GlampingPostRequestDto dto = new GlampingPostRequestDto();
        dto.setOwnerId(3);
        dto.setGlampName("그린 글램핑");
        dto.setGlampLocation("대구광역시 중구 109-2");
        dto.setRegion("gyeongbuk");
        dto.setGlampingImg("img");
        dto.setIntro("공기 좋은 그린 글램핑입니다.");
        dto.setBasic("입실은 15시부터 퇴실은 12시까지 가능합니다.");
        dto.setNotice("취사를 금지합니다.");
        dto.setTraffic("해수욕장 10분");

        String reqJson = om.writeValueAsString(dto);

        Map<String, Object> expectedRes = new HashMap<>();
        expectedRes.put("code", "SU");
        expectedRes.put("message", "등록을 완료하였습니다.");

        String expectedResJson = om.writeValueAsString(expectedRes);

        MockMultipartFile mockFile = new MockMultipartFile(
                "glampImg", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());

        MockMultipartFile jsonFile = new MockMultipartFile(
                "req", "", MediaType.APPLICATION_JSON_VALUE, reqJson.getBytes());

        mvc.perform(multipart(BASE_URL)
                        .file(jsonFile)
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResJson))
                .andDo(print());

        verify(service).postGlampingInfo(dto, mockFile);


//        GlampingPostRequestDto dto = new GlampingPostRequestDto();
//        dto.setOwnerId(3);
//        dto.setGlampName("그린 글램핑");
//        dto.setGlampLocation("대구광역시 중구 109-2");
//        dto.setRegion("gyeongbuk");
//        dto.setGlampingImg("img");
//        dto.setIntro("공기 좋은 그린 글램핑입니다.");
//        dto.setBasic("입실은 15시부터 퇴실은 12시까지 가능합니다.");
//        dto.setNotice("취사를 금지합니다.");
//        dto.setTraffic("해수욕장 10분");
//
//        String reqJson = om.writeValueAsString(dto);
//
//        Map<String, Object> expectedRes = new HashMap();
//        expectedRes.put("code", "SU");
//        expectedRes.put("message", "등록을 완료하였습니다.");
//
//        String expectedResJson = om.writeValueAsString(expectedRes);
//
//
//        mvc.perform(
//                        post(BASE_URL)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(reqJson)
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().json(expectedResJson))
//                .andDo(print());
//
//        verify(service).postGlampingInfo(dto,null);
    }

    @Test
    void deleteRoom() {
    }

    @Test
    void getRoomList() {
    }

    @Test
    void updateOwnerInfo() {
    }
}