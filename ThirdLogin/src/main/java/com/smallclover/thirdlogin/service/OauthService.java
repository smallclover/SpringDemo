package com.smallclover.thirdlogin.service;

public interface OauthService {

    String choiceLoginType(String loginType);

    String getOauthToken(String loginType, String code);

    String getUserInfo(String loginType, String accessToken);
}
