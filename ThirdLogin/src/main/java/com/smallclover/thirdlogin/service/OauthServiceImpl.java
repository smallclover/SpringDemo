package com.smallclover.thirdlogin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.smallclover.thirdlogin.constant.ThirdLoginConstant;
import com.smallclover.thirdlogin.properties.GithubProperties;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpOptions;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OauthServiceImpl implements OauthService{

    @Autowired
    GithubProperties githubProperties;

    @Override
    public String choiceLoginType(String loginType) {
        String url = "";
        if(ThirdLoginConstant.GITHUB.equals(loginType)){
            url = ThirdLoginConstant.GITHUB_URL
                    .replace("{client_id}", githubProperties.getClientId())
                    .replace("{redirect_uri}", githubProperties.getRedirectUrl());
        }
        return url;
    }

    @Override
    public String getOauthToken(String loginType, String code) {
        Map<String,String> map = new HashMap<>();
        String result = "";
        if(ThirdLoginConstant.GITHUB.equals(loginType)){
            String url = ThirdLoginConstant.GITHUB_OAUTH_TOKEN_URL;
            map.put("client_id", githubProperties.getClientId());
            map.put("client_secret", githubProperties.getClientSecret());
            map.put("code", code);

            result = sendPostRequest(url, map);
        }
        return result;
    }

    @Override
    public String getUserInfo(String loginType, String accessToken) {
        String userInfo = "";
        if (ThirdLoginConstant.GITHUB.equals(loginType)){
            String url = ThirdLoginConstant.GITHUB_USER_INFO_URL;

            Header header = new BasicHeader(HttpHeaders.AUTHORIZATION, "token "+accessToken);
            Header[] headers = new Header[]{header};
            userInfo = sentGetRequest(url, headers);
        }
        return userInfo;
    }


    private String sendPostRequest(String url, Map<String, String> params){
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                String json = new ObjectMapper().writeValueAsString(params);

                StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
                ClassicHttpRequest httpPost = ClassicRequestBuilder.post(url)
                        .setHeader(new BasicHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON))
                        .setEntity(stringEntity)
                        .build();
                return httpClient.execute(httpPost, response -> {
                    final HttpEntity entity = response.getEntity();
                    String result = EntityUtils.toString(entity);
                    // 不消费完内容，无法取到响应内容
                    EntityUtils.consume(entity);
                    return result;
                });
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    private String sentGetRequest(String url, Header[] headers){
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            ClassicHttpRequest httpGet = ClassicRequestBuilder.get(url)
                    .setHeaders(headers)
                    .build();
            return httpClient.execute(httpGet, response -> {
                final HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
                return result;
            });
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
