package client.model.templates;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class Description {

    @SerializedName("en")
    private String en = null;

    @SerializedName("pl")
    private String pl = null;

    public Description en(String en) {
        this.en = en;
        return this;
    }

    /**
     * Id
     * @return id
     **/
    @ApiModelProperty(value = "En")
    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public Description pl(String pl) {
        this.pl = pl;
        return this;
    }

    /**
     * Name
     * @return name
     **/
    @ApiModelProperty(value = "Pl")
    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Description description = (Description) o;
        return Objects.equals(this.en, description.en) &&
                Objects.equals(this.pl, description.en);
    }

    @Override
    public int hashCode() {
        return Objects.hash(en, pl);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Desciption {\n");
        sb.append("    en: ").append(toIndentedString(en)).append("\n");
        sb.append("    pl: ").append(toIndentedString(pl)).append("\n");
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
