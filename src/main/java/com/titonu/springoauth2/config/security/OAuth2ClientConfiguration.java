package com.titonu.springoauth2.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.ArrayList;
import java.util.List;

@EnableOAuth2Client
@Configuration
public class OAuth2ClientConfiguration {
    private Logger logger = LoggerFactory.getLogger(OAuth2ClientConfiguration.class);

    @Value("${oauth.resource:${server.address}:${server.port}}")
    private String baseUrl;
    @Value("${oauth.authorize:${server.address}:${server.port}/oauth/authorize}")
    private String authorizeUrl;
    @Value("${oauth.token:http://${server.address}:${server.port}/oauth/token}")
    protected String tokenUrl;
    protected String userAndAppsId;
    protected String password;
    @Value("${security.oauth2.client.client-id}")
    protected String clientId;
    @Value("${security.oauth2.client.client-secret}")
    protected String clientSecret;
    @Value("${security.oauth2.client.grant-type}")
    private String grant_type;
    @Value("${security.oauth2.resource.id}")
    private String resourceId;

    @Bean
    protected OAuth2ProtectedResourceDetails resource() {

        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
        List<String> scopes = new ArrayList<>(2);
        scopes.add("write");
        scopes.add("read");
        scopes.add("trust");

        resource.setAccessTokenUri(tokenUrl);
        resource.setId(resourceId);
        resource.setClientId(clientId);
        resource.setClientSecret(clientSecret);
        resource.setGrantType(grant_type);
        resource.setScope(scopes);
        resource.setUsername(userAndAppsId);
        resource.setPassword(password);
        return resource;
    }

    @Bean
    public OAuth2RestOperations oAuth2RestOperations() {
        AccessTokenRequest atr = new DefaultAccessTokenRequest();
        return new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(atr));
    }
}
