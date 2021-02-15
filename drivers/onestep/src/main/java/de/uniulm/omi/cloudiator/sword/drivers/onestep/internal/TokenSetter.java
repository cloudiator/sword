package de.uniulm.omi.cloudiator.sword.drivers.onestep.internal;

import client.api.ApiClient;
import client.auth.OAuth;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TokenSetter {

    private static Logger LOGGER = LoggerFactory.getLogger(TokenSetter.class);

    private ApiClient apiClient;
    private String username;
    private String password;

    public TokenSetter(ApiClient apiClient, String username, String password) {
        this.apiClient = apiClient;
        this.username = username;
        this.password = password;
    }

    public void setToken() {
        TokenResponse tokenResponse;
        try {
            tokenResponse = getToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        OAuth oauth2 = (OAuth) apiClient.getAuthentication("oauth2");
        oauth2.setAccessToken(tokenResponse.getToken());
        LOGGER.info("AccessToken: " + oauth2.getAccessToken());
    }

    private TokenResponse getToken() throws IOException {
        String json = "{ \"email\": \""+ username + "\", \"password\": \"" +
                password + "\", \"remember_me\": \"" + true + "\" }";
        return getTokenResponse(new StringEntity(json));
    }

    private TokenResponse getTokenResponse(HttpEntity httpEntity) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.custom().build()) {
            HttpPost httpPost = new HttpPost("https://panel.onestepcloud.pl/api/sign_in");
            httpPost.setEntity(httpEntity);
            httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                String res = EntityUtils.toString(response.getEntity());
                return new Gson().fromJson(res, TokenResponse.class);
            }

        }
    }
}
