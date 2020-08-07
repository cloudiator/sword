package client.model.account;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * SshKey
 */
@ApiModel(description = "SshKey Response")
public class SshKeyResponse {
  @SerializedName("id")
  private Integer id = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("fingerprint")
  private String fingerprint = null;

  @SerializedName("created_at")
  private String createdAt = null;

  public SshKeyResponse id(Integer id) {
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

  public SshKeyResponse name(String name) {
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

  public SshKeyResponse fingerprint(String fingerprint) {
    this.fingerprint = fingerprint;
    return this;
  }

  /**
   * Fingerprint
   * @return name
   **/
  @ApiModelProperty(value = "Fingerprint")
  public String getFingerprint() {
    return fingerprint;
  }

  public void setFingerprint(String fingerprint) {
    this.fingerprint = fingerprint;
  }

  public SshKeyResponse createdAt(String createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * CreatedAt
   * @return CreatedAt
   **/
  @ApiModelProperty(value = "CreatedAt")
  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SshKeyResponse sshKeyResponse = (SshKeyResponse) o;
    return Objects.equals(this.id, sshKeyResponse.id) &&
            Objects.equals(this.name, sshKeyResponse.name) &&
            Objects.equals(this.fingerprint, sshKeyResponse.fingerprint) &&
            Objects.equals(this.createdAt, sshKeyResponse.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, fingerprint, createdAt);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SshKeyResponse {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    fingerprint: ").append(toIndentedString(fingerprint)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

