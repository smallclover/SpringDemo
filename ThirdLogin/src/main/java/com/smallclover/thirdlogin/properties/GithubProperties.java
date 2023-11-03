package com.smallclover.thirdlogin.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "github")
public class GithubProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUrl;
}
