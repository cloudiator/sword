package client.model.instances;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class Disk {

    @SerializedName("size")
    private Integer size = null;

    @SerializedName("type")
    private String type = null;

    public Disk size(Integer size) {
        this.size = size;
        return this;
    }

    /**
     * Id
     * @return id
     **/
    @ApiModelProperty(value = "size")
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Disk type(String type) {
        this.type = type;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Disk disk = (Disk) o;
        return Objects.equals(this.size, disk.size) &&
                Objects.equals(this.type, disk.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, type);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Disk  {\n");

        sb.append("    size: ").append(toIndentedString(size)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
