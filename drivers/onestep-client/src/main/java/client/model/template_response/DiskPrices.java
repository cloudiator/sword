package client.model.template_response;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * DiskPrices
 */
@ApiModel(description = "DiskPrices")
public class DiskPrices {
    @SerializedName("hdd")
    private Price hdd = null;

    @SerializedName("ssd")
    private Price ssd = null;

    public DiskPrices hdd(Price hdd) {
        this.hdd = hdd;
        return this;
    }

    /**
     * Id
     * @return id
     **/
    @ApiModelProperty(value = "Hdd")
    public Price getHdd() {
        return hdd;
    }

    public void setHdd(Price hdd) {
        this.hdd = hdd;
    }

    public DiskPrices ssd(Price ssd) {
        this.ssd = ssd;
        return this;
    }

    /**
     * Name
     * @return name
     **/
    @ApiModelProperty(value = "Ssd")
    public Price getSsd() {
        return ssd;
    }

    public void setSsd(Price ssd) {
        this.ssd = ssd;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DiskPrices prices = (DiskPrices) o;
        return Objects.equals(this.hdd, prices.hdd) &&
                Objects.equals(this.ssd, prices.ssd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hdd, ssd);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DiskPrices {\n");
        sb.append("    hdd: ").append(toIndentedString(hdd)).append("\n");
        sb.append("    ssd: ").append(toIndentedString(ssd)).append("\n");
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

