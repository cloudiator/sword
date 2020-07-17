package de.uniulm.omi.cloudiator.sword.onestep.internal;


import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenResponse {

    @SerializedName("username")
    private String username;

    @SerializedName("authentication_token")
    private String authentication_token;

    @SerializedName("primary")
    private boolean primary;

    @Override
    public String toString() {
        return "TokenResponse{" +
                "username='" + username + '\'' +
                ", authentication_token=" + authentication_token +
                ", primary='" + primary +
                '}';
    }
}
