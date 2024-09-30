package com.shoping.payment_service.external.intercept;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

@Configuration
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    public RestTemplateInterceptor(OAuth2AuthorizedClientManager man) {
        this.oAuth2AuthorizedClientManager = man;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        request.getHeaders().add("Authorization",
                "Bearer " +
                        oAuth2AuthorizedClientManager
                                .authorize(OAuth2AuthorizeRequest
                                        .withClientRegistrationId("internal-client")
                                        .principal("internal")
                                        .build())
                                .getAccessToken().getTokenValue());
        return execution.execute(request, body);
    }
}
