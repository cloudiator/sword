package client.model;

import client.RegionState;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(description = "Region")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2020-02-03T15:23:33.358+01:00")
public class Region {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("state")
    private RegionState state;

    @SerializedName("isActive")
    private boolean isActive;

    public Region id(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * Id
     *
     * @return id
     **/
    @ApiModelProperty(value = "Id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Region name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Name
     *
     * @return name
     **/
    @ApiModelProperty(value = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Region FullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    /**
     * fullName
     *
     * @return fullname
     **/
    @ApiModelProperty(value = "fullName")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Region state(RegionState regionState) {
        this.state = regionState;
        return this;
    }

    /**
     * state
     *
     * @return state
     **/
    @ApiModelProperty(value = "State")
    public RegionState getState() {
        return state;
    }

    public void setState(RegionState state) {
        this.state = state;
    }

    public Region isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    /**
     * Is active
     *
     * @return isActive
     **/
    @ApiModelProperty(value = "Is active")
    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Region region = (Region) o;
        return Objects.equals(this.id, region.id) &&
                Objects.equals(this.name, region.name) &&
                Objects.equals(this.fullName, region.fullName) &&
                Objects.equals(this.state, region.state) &&
                Objects.equals(this.isActive, region.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isActive);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class rregion {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    fullName: ").append(toIndentedString(fullName)).append("\n");
        sb.append("    state: ").append(toIndentedString(state)).append("\n");
        sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
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
