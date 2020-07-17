package client.model.template_response;

import com.google.gson.annotations.SerializedName;
import com.oktawave.api.client.model.Object;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * PrivateNetwork
 */
@ApiModel(description = "ImageTemplate")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2020-02-03T15:23:33.358+01:00")
public class PrivateNetwork {
    @SerializedName("id")
    private Integer id = null;

    @SerializedName("state")
    private String state = null;

    @SerializedName("name")
    private String name = null;

    @SerializedName("fingerprint")
    private String network = null;

    public PrivateNetwork id(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * Id
     * @return id
     **/
    @ApiModelProperty(value = "Id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PrivateNetwork state(String state) {
        this.state = state;
        return this;
    }

    /**
     * Id
     * @return id
     **/
    @ApiModelProperty(value = "State")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public PrivateNetwork name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Name
     * @return name
     **/
    @ApiModelProperty(value = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PrivateNetwork network(String network) {
        this.network = network;
        return this;
    }

    /**
     * Fingerprint
     * @return name
     **/
    @ApiModelProperty(value = "Fingerprint")
    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrivateNetwork privateNetwork = (PrivateNetwork) o;
        return Objects.equals(this.id, privateNetwork.id) &&
                Objects.equals(this.state, privateNetwork.state) &&
                Objects.equals(this.name, privateNetwork.name) &&
                Objects.equals(this.network, privateNetwork.network);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, network);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SshKey {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    fingerprint: ").append(toIndentedString(network)).append("\n");
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

