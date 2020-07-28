package de.uniulm.omi.cloudiator.sword.drivers.onestep.internal;


import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenResponse {

    @SerializedName("token")
    private String token;

    @SerializedName("locale")
    private String locale;

    @Override
    public String toString() {
        return "TokenResponse{" +
                ", token=" + token +
                ", locale='" + locale +
                '}';
    }
}
