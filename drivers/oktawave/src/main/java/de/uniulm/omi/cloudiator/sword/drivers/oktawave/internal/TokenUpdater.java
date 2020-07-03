package de.uniulm.omi.cloudiator.sword.drivers.oktawave.internal;

import com.google.gson.Gson;
import com.oktawave.api.client.ApiClient;
import com.oktawave.api.client.auth.OAuth;
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
import java.util.Base64;

public class TokenUpdater implements Runnable {

    private static Logger LOGGER = LoggerFactory.getLogger(TokenUpdater.class);
    private static final int TIME = 10;

    private ApiClient apiClient;
    private String clientId;
    private String clientSecret;
    private String username;
    private String password;

    private TokenResponse tempToken;

    public TokenUpdater(ApiClient apiClient, String clientId, String clientSecret, String username, String password) {
        this.apiClient = apiClient;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.username = username;
        this.password = password;

        try {
            tempToken = getToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setToken(tempToken);
    }

    @Override
    public void run() {

        try {
            TokenResponse tokenResponse = tempToken;

            while (!Thread.currentThread().isInterrupted()) {

                String refreshToken = tokenResponse.getRefreshToken();
                if (refreshToken != null && !"".equals(refreshToken)) {
                    try {
                        Thread.sleep(getDelay(tokenResponse.getExpiresIn()));

                        LOGGER.info("Refreshing token - begin");
                        tokenResponse = refreshToken(refreshToken);
                        setToken(tokenResponse);
                        LOGGER.info("Refreshing token - end");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error during getting token", e);
            Thread.currentThread().interrupt();
        }
    }

    private void setToken(TokenResponse tokenResponse) {
        OAuth oauth2 = (OAuth) apiClient.getAuthentication("oauth2");
        oauth2.setAccessToken(tokenResponse.getAccessToken());
        LOGGER.info("AccessToken: " + oauth2.getAccessToken());
    }

    private TokenResponse getToken() throws IOException {
        return getTokenResponse(new StringEntity("grant_type=password&username=" + username + "&password=" + password + "&scope=oktawave.api offline_access"));
    }

    private TokenResponse refreshToken(String refreshToken) throws IOException {
        return getTokenResponse(new StringEntity("grant_type=refresh_token&refresh_token=" + refreshToken));
    }

    private TokenResponse getTokenResponse(HttpEntity httpEntity) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.custom().build()) {
            HttpPost httpPost = new HttpPost("https://id.oktawave.com/core/connect/token");
            httpPost.setEntity(httpEntity);
            httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            httpPost.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()));

            try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
                String res = EntityUtils.toString(response.getEntity());
                return new Gson().fromJson(res, TokenResponse.class);
            }
        }
    }


    private long getDelay(int expiresIn) {
        if (expiresIn - TIME < 0) {
            //TODO - in extremly situations this could cause exceptions
            return expiresIn * 1000;
        } else {
            return (expiresIn - TIME) * 1000;
        }
    }

}
