/*
 * Oktawave API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package client.model.instances;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

/**
 * ApiCollectionInstance
 */
public class ApiCollectionInstance {
  @SerializedName("virtual_machines")
  private List<Instance> instances = null;

   /**
   * Get items
   * @return items
  **/
  @ApiModelProperty(value = "")
  public List<Instance> getInstances() {
    return instances;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiCollectionInstance apiCollectionInstance = (ApiCollectionInstance) o;
    return Objects.equals(this.instances, apiCollectionInstance.instances) ;
  }

  @Override
  public int hashCode() {
    return Objects.hash(instances);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiCollectionInstance {\n");

      sb.append("    virtual_machines: ").append(toIndentedString(instances)).append("\n");
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

