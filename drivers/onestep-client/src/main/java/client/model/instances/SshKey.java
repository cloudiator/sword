package client.model.instances;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class SshKey {

    @SerializedName("id")
    private Integer id = null;

    @SerializedName("public_key")
    private String publicKey = null;

    public SshKey id(Integer size) {
        this.id = size;
        return this;
    }

    /**
     * Id
     * @return id
     **/
    @ApiModelProperty(value = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SshKey publicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    /**
     * Id
     * @return id
     **/
    @ApiModelProperty(value = "publicKey")
    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SshKey sshKey = (SshKey) o;
        return Objects.equals(this.id, sshKey.id) &&
                Objects.equals(this.publicKey, sshKey.publicKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publicKey);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SshKey  {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    public_key: ").append(toIndentedString(publicKey)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
