package client.model.template_response;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

/**
 * Cluster
 */
@ApiModel(description = "Cluster")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2020-02-03T15:23:33.358+01:00")
public class Cluster {
    @SerializedName("id")
    private Integer id = null;

    @SerializedName("name")
    private String name = null;

    @SerializedName("description")
    private String description = null;

    @SerializedName("max_additional_disks")
    private Integer maxAdditionalDisks = null;

    @SerializedName("licences_price")
    private Price licencesPrice = null;

    @SerializedName("resources")
    private ResourcesSet resources = null;

    public Cluster id(Integer id) {
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

    public Cluster name(String name) {
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

    public Cluster description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Description
     * @return description
     **/
    @ApiModelProperty(value = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Cluster maxAdditionalDisks(String maxAdditionalDisks) {
        this.description = description;
        return this;
    }

    /**
     * Description
     * @return description
     **/
    @ApiModelProperty(value = "Description")
    public Integer getMaxAdditionalDisks() {
        return maxAdditionalDisks;
    }

    public void setMaxAdditionalDisks(Integer maxAdditionalDisks) {
        this.maxAdditionalDisks = maxAdditionalDisks;
    }

    public Cluster licencesPrice(String licencesPrice) {
        this.description = description;
        return this;
    }

    /**
     * Description
     * @return description
     **/
    @ApiModelProperty(value = "LicencesPrice")
    public Price getLicencesPrice() {
        return licencesPrice;
    }

    public void setLicencesPrice(Price licencesPrice) {
        this.licencesPrice = licencesPrice;
    }

    public Cluster recoucesSet(ResourcesSet resources) {
        this.resources = resources;
        return this;
    }

    /**
     * Description
     * @return description
     **/
    @ApiModelProperty(value = "LicencesPrice")
    public ResourcesSet getResources() {
        return resources;
    }

    public void setLicencesPrice(ResourcesSet resources) {
        this.resources = resources;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cluster cluster = (Cluster) o;
        return Objects.equals(this.id, cluster.id) &&
                Objects.equals(this.name, cluster.name) &&
                Objects.equals(this.description, cluster.description) &&
                Objects.equals(this.maxAdditionalDisks, cluster.maxAdditionalDisks) &&
                Objects.equals(this.licencesPrice, cluster.licencesPrice) &&
                Objects.equals(this.resources, cluster.resources);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, maxAdditionalDisks, licencesPrice, resources);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Cluster {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    maxAdditionalDisks: ").append(toIndentedString(maxAdditionalDisks)).append("\n");
        sb.append("    licencesPrice: ").append(toIndentedString(licencesPrice)).append("\n");
        sb.append("    resources: ").append(toIndentedString(resources)).append("\n");
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

