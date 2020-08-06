package client.model.instances;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class IpAddress {
    @SerializedName("id")
    private Integer id = null;

    @SerializedName("ip_address")
    private String ipAddress = null;

    public IpAddress id(Integer id) {
        this.id = id;
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

    public IpAddress type(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    /**
     * Id
     * @return id
     **/
    @ApiModelProperty(value = "ip address")
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IpAddress ipAddress = (IpAddress) o;
        return Objects.equals(this.id, ipAddress.id) &&
                Objects.equals(this.ipAddress, ipAddress.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ipAddress);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IpAddress  {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    ipAddress: ").append(toIndentedString(ipAddress)).append("\n");
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
