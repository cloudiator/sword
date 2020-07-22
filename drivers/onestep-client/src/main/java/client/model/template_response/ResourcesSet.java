package client.model.template_response;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * ResourceSet
 */
@ApiModel(description = "ResourceSet")
public class ResourcesSet {
    @SerializedName("cpu")
    private Resource cpu = null;

    @SerializedName("ram")
    private Resource ram = null;

    @SerializedName("primary_disk")
    private DiskResource primaryDisk = null;

    @SerializedName("additional_disk")
    private DiskResource additionalDisk = null;

    public ResourcesSet cpu(Resource cpu) {
        this.cpu = cpu;
        return this;
    }

    /**
     * Id
     * @return id
     **/
    @ApiModelProperty(value = "Id")
    public Resource getCpu() {
        return cpu;
    }

    public void setCpu(Resource cpu) {
        this.cpu = cpu;
    }

    public ResourcesSet ram(Resource ram) {
        this.ram = ram;
        return this;
    }

    /**
     * Name
     * @return name
     **/
    @ApiModelProperty(value = "Name")
    public Resource getRam() {
        return ram;
    }

    public void setRam(Resource ram) {
        this.ram = ram;
    }

    public ResourcesSet primaryDisk(DiskResource primaryDisk) {
        this.primaryDisk = primaryDisk;
        return this;
    }

    /**
     * Fingerprint
     * @return name
     **/
    @ApiModelProperty(value = "Price")
    public DiskResource getPrimaryDisk() {
        return primaryDisk;
    }

    public void setPrimaryDisk(DiskResource primaryDisk) {
        this.primaryDisk = primaryDisk;
    }

    public ResourcesSet additionalDisk(DiskResource additionalDisk) {
        this.additionalDisk = additionalDisk;
        return this;
    }

    /**
     * Id
     * @return id
     **/
    @ApiModelProperty(value = "Id")
    public DiskResource getAdditionalDisk() {
        return additionalDisk;
    }

    public void setAdditionalDisk(DiskResource additionalDisk) {
        this.additionalDisk = additionalDisk;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResourcesSet resource = (ResourcesSet) o;
        return Objects.equals(this.cpu, resource.cpu) &&
                Objects.equals(this.ram, resource.ram) &&
                Objects.equals(this.primaryDisk, resource.primaryDisk) &&
                Objects.equals(this.additionalDisk, resource.additionalDisk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpu, ram, primaryDisk, additionalDisk);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ResourceSet {\n");
        sb.append("    cpu: ").append(toIndentedString(cpu)).append("\n");
        sb.append("    ram: ").append(toIndentedString(ram)).append("\n");
        sb.append("    primaryDisk: ").append(toIndentedString(primaryDisk)).append("\n");
        sb.append("    additionalDisk: ").append(toIndentedString(additionalDisk)).append("\n");
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

