package client.model.instances;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class OperatingSystem {

    @SerializedName("name")
    private String name = null;

    @SerializedName("version")
    private String version = null;

    public OperatingSystem name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Id
     * @return id
     **/
    @ApiModelProperty(value = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OperatingSystem version(String version) {
        this.version = version;
        return this;
    }

    /**
     * Version
     * @return version
     **/
    @ApiModelProperty(value = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OperatingSystem operatingSystem = (OperatingSystem) o;
        return Objects.equals(this.name, operatingSystem.name) &&
                Objects.equals(this.version, operatingSystem.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OperatingSystem  {\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
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
