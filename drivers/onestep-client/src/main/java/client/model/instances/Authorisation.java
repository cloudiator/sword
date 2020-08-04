package client.model.instances;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

public class Authorisation {

   @SerializedName("type")
    private String type = null;

    @SerializedName("ssk_keys")
    private List<SshKey> sshKeys = null;

    public Authorisation type(String id) {
        this.type = id;
        return this;
    }

    /**
     * Id
     * @return id
     **/
    @ApiModelProperty(value = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @ApiModelProperty(value = "ssh keys")
    public List<SshKey> getSshKeys() {
        return sshKeys;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Authorisation authorisation = (Authorisation) o;
        return Objects.equals(this.type, authorisation.type) &&
                Objects.equals(this.sshKeys, authorisation.sshKeys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, sshKeys);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Authorisation {\n");

        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    sshKeys: ").append(toIndentedString(sshKeys)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
