package com.smallclover.thirdlogin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallclover.thirdlogin.dto.ThirdlyResult;
import com.smallclover.thirdlogin.service.OauthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/oauth")
public class OauthController {
    @Autowired
    OauthService oauthService;


    @GetMapping("/login/{loginType}")
    public void thirdlyLogin(@PathVariable("loginType") String loginType, HttpServletResponse response) throws IOException {
        String url = oauthService.choiceLoginType(loginType);
        log.info(url);
        response.sendRedirect(url);
    }

    @GetMapping("/{loginType}/callback")
    public String redirectUri(@PathVariable("loginType") String loginType, String code) {
        log.info("code==>{}", code);
        String result = oauthService.getOauthToken(loginType, code);
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        ThirdlyResult thirdlyResult = null;
        try {
            thirdlyResult = om.readValue(result, ThirdlyResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String userInfo = oauthService.getUserInfo(loginType, thirdlyResult.getAccessToken());
        return userInfo;
    }
}

