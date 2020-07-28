package client.model.template_response;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * Resource
 */
@ApiModel(description = "Resource")
public class Resource {
    @SerializedName("min")
    private Integer min = null;

    @SerializedName("max")
    private Integer max = null;

    @SerializedName("price")
    private Price price = null;

    public Resource min(Integer min) {
        this.min = min;
        return this;
    }

    /**
     * Id
     * @return id
     **/
    @ApiModelProperty(value = "Min")
    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Resource max(Integer max) {
        this.max = max;
        return this;
    }

    /**
     * Name
     * @return name
     **/
    @ApiModelProperty(value = "Max")
    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Resource price(Price price) {
        this.price = price;
        return this;
    }

    /**
     * Fingerprint
     * @return name
     **/
    @ApiModelProperty(value = "Price")
    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Resource resource = (Resource) o;
        return Objects.equals(this.min, resource.min) &&
                Objects.equals(this.max, resource.max) &&
                Objects.equals(this.price, resource.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max, price);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Resource {\n");
        sb.append("    id: ").append(toIndentedString(min)).append("\n");
        sb.append("    name: ").append(toIndentedString(max)).append("\n");
        sb.append("    price: ").append(toIndentedString(price)).append("\n");
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

