package client.model.templates;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * DiskResource
 */
@ApiModel(description = "DiskResource")
public class DiskResource {
    @SerializedName("min")
    private Integer min = null;

    @SerializedName("max")
    private Integer max = null;

    @SerializedName("price")
    private DiskPrices price = null;

    @SerializedName("cents")
    private Integer cents = null;

    @SerializedName("currency")
    private String currency = null;

    public DiskResource min(Integer min) {
        this.min = min;
        return this;
    }

    /**
     * Id
     * @return id
     **/
    @ApiModelProperty(value = "Id")
    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public DiskResource max(Integer max) {
        this.max = max;
        return this;
    }

    /**
     * Name
     * @return name
     **/
    @ApiModelProperty(value = "Name")
    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public DiskResource price(DiskPrices price) {
        this.price = price;
        return this;
    }

    /**
     * Fingerprint
     * @return name
     **/
    @ApiModelProperty(value = "Price")
    public DiskPrices getPrice() {
        return price;
    }

    public void setPrice(DiskPrices price) {
        this.price = price;
    }

    public DiskResource cents(Integer cents) {
        this.cents = cents;
        return this;
    }

    /**
     * Id
     * @return id
     **/
    @ApiModelProperty(value = "Id")
    public Integer getCents() {
        return cents;
    }

    public void setCents(Integer cents) {
        this.cents = cents;
    }

    public DiskResource currency(String currency) {
        this.currency = currency;
        return this;
    }

    /**
     * Name
     * @return name
     **/
    @ApiModelProperty(value = "Name")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiskResource resource = (DiskResource) o;
        return Objects.equals(this.min, resource.min) &&
                Objects.equals(this.max, resource.max) &&
                Objects.equals(this.price, resource.price) &&
                Objects.equals(this.cents, resource.cents) &&
                Objects.equals(this.currency, resource.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max, price, cents, currency);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DiskResource {\n");
        sb.append("    id: ").append(toIndentedString(min)).append("\n");
        sb.append("    name: ").append(toIndentedString(max)).append("\n");
        sb.append("    price: ").append(toIndentedString(price)).append("\n");
        sb.append("    cents: ").append(toIndentedString(cents)).append("\n");
        sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
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

