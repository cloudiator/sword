package de.uniulm.omi.cloudiator.sword.drivers.oktawave.internal;


import com.google.gson.annotations.SerializedName;

public class TokenResponse {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("expires_in")
    private int expiresIn;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("refresh_token")
    private String refreshToken;


    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", tokenType='" + tokenType + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
