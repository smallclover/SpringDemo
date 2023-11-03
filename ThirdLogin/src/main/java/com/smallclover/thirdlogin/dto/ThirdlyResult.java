package com.smallclover.thirdlogin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdlyResult {

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "token_type")
    private String tokenType;

//    private Long expiresIn;

//    private String refreshToken;

    @JsonProperty(value = "scope")
    private String scope;

//    private String createAt;

}
