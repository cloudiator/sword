package client.model.template_response;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * Price
 */
@ApiModel(description = "Price")
public class Price {
    @SerializedName("cents")
    private Integer cents = null;

    @SerializedName("currency")
    private String currency = null;

    public Price cents(Integer cents) {
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

    public Price currency(String currency) {
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
        Price publicIpPrice = (Price) o;
        return Objects.equals(this.cents, publicIpPrice.cents) &&
                Objects.equals(this.currency, publicIpPrice.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cents, currency);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Price {\n");
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

