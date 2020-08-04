package client.model.templates;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * Cluster
 */
@ApiModel(description = "Cluster")
public class Cluster {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name = null;

    @SerializedName("description")
    private Description description = null;

    @SerializedName("max_additional_disks")
    private int maxAdditionalDisks;

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

    public Cluster description(Description description) {
        this.description = description;
        return this;
    }

    /**
     * Description
     * @return description
     **/
    @ApiModelProperty(value = "Description")
    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public Cluster maxAdditionalDisks(int maxAdditionalDisks) {
        this.maxAdditionalDisks = maxAdditionalDisks;
        return this;
    }

    /**
     * Description
     * @return description
     **/
    @ApiModelProperty(value = "MaxAdditionalDisks")
    public int getMaxAdditionalDisks() {
        return maxAdditionalDisks;
    }

    public void setMaxAdditionalDisks(int maxAdditionalDisks) {
        this.maxAdditionalDisks = maxAdditionalDisks;
    }

    public Cluster licencesPrice(Price licencesPrice) {
        this.licencesPrice = licencesPrice;
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

    public Cluster resourcesSet(ResourcesSet resources) {
        this.resources = resources;
        return this;
    }

    /**
     * Description
     * @return description
     **/
    @ApiModelProperty(value = "ResourcesSet ")
    public ResourcesSet getResources() {
        return resources;
    }

    public void setResourcesSet(ResourcesSet resources) {
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

