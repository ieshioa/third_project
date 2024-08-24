package com.green.glampick.common.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.List;

@Getter
@ConfigurationProperties(prefix = "app")
@RequiredArgsConstructor
public class AppProperties {

    private final Jwt jwt;
    private final Oauth2 oauth2;

    @Getter
    @Setter
    public static class Jwt {
        private String secret;
        private String headerSchemaName;
        private String tokenType;
        private long accessTokenExpiry;
        private long refreshTokenExpiry;
        private String refreshTokenCookieName;
        private int refreshTokenCookieMaxAge;

        @ConstructorBinding
        public Jwt(String secret, String headerSchemaName, String tokenType, long accessTokenExpiry, long refreshTokenExpiry, String refreshTokenCookieName) {
            this.secret = secret;
            this.headerSchemaName = headerSchemaName;
            this.tokenType = tokenType;
            this.accessTokenExpiry = accessTokenExpiry;
            this.refreshTokenExpiry = refreshTokenExpiry;
            this.refreshTokenCookieName = refreshTokenCookieName;
            this.refreshTokenCookieMaxAge = (int)(refreshTokenExpiry * 0.001); // ms > s 변환
        }

    }

    @Getter
    @Setter
    public static class Oauth2 {
        private String baseUri;
        private String authorizationRequestCookieName;
        private String redirectUriParamCookieName;
        private int cookieExpirySeconds;
        private List<String> authorizedRedirectUris;
    }

}
