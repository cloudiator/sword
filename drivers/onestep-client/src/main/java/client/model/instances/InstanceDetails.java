package client.model.instances;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

public class InstanceDetails {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name = null;

    @SerializedName("description")
    private String description = null;

    @SerializedName("cpu_cores")
    private int cpuCores;

    @SerializedName("ram")
    private int ram ;

    @SerializedName("storage")
    private int storage;

    @SerializedName("state")
    private String state = null;

    @SerializedName("cluster")
    private String cluster = null;

    @SerializedName("created_at")
    private String createdAt = null;

    @SerializedName("operating_system")
    private OperatingSystem operatingSystem = null;

    @SerializedName("private_ip_addresses")
    private List<IpAddress> privateIpAddresses = null;

    @SerializedName("public_ip_addresses")
    private List<IpAddress> publicIpAddresses = null;

    public InstanceDetails id(Integer id) {
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

    public InstanceDetails name(String name) {
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

    public InstanceDetails description(String description) {
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

    public InstanceDetails cpuCores(int cpuCores) {
        this.cpuCores = cpuCores;
        return this;
    }

    /**
     * cpuCores
     * @return cpuCores
     **/
    @ApiModelProperty(value = "cpuCores")
    public int getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(int cpuCores) {
        this.cpuCores = cpuCores;
    }

    public InstanceDetails ram(int ram) {
        this.ram = ram;
        return this;
    }

    /**
     * ram
     * @return ram
     **/
    @ApiModelProperty(value = "ram")
    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public InstanceDetails storage(int storage) {
        this.storage = storage;
        return this;
    }

    /**
     * storage
     * @return storage
     **/
    @ApiModelProperty(value = "storage")
    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public InstanceDetails state(String state) {
        this.state = state;
        return this;
    }

    /**
     * state
     * @return state
     **/
    @ApiModelProperty(value = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public InstanceDetails cluster(String cluster) {
        this.cluster = cluster;
        return this;
    }

    /**
     * cluster
     * @return cluster
     **/
    @ApiModelProperty(value = "cluster")
    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public InstanceDetails createdAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * createdAt
     * @return createdAt
     **/
    @ApiModelProperty(value = "createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public InstanceDetails createdAt(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
        return this;
    }

    /**
     * createdAt
     * @return createdAt
     **/
    @ApiModelProperty(value = "operatingSystem")
    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    @ApiModelProperty(value = "private addresses")
    public List<IpAddress> getPrivateAddresses() {
        return privateIpAddresses;
    }

    @ApiModelProperty(value = "public addresses")
    public List<IpAddress> getPublicAddresses() {
        return publicIpAddresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceDetails instance = (InstanceDetails) o;
        return Objects.equals(this.id, instance.id) &&
                Objects.equals(this.name, instance.name) &&
                Objects.equals(this.description, instance.description) &&
                Objects.equals(this.cpuCores, instance.cpuCores) &&
                Objects.equals(this.ram, instance.ram) &&
                Objects.equals(this.storage, instance.storage) &&
                Objects.equals(this.state, instance.state) &&
                Objects.equals(this.cluster, instance.cluster) &&
                Objects.equals(this.createdAt, instance.createdAt) &&
                Objects.equals(this.operatingSystem, instance.operatingSystem) &&
                Objects.equals(this.privateIpAddresses, instance.privateIpAddresses) &&
                Objects.equals(this.publicIpAddresses, instance.publicIpAddresses) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, cpuCores, ram, storage, state, cluster, createdAt, privateIpAddresses, publicIpAddresses);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class InstanceDetails {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    cpuCores: ").append(toIndentedString(cpuCores)).append("\n");
        sb.append("    ram: ").append(toIndentedString(ram)).append("\n");
        sb.append("    storage: ").append(toIndentedString(storage)).append("\n");
        sb.append("    state: ").append(toIndentedString(state)).append("\n");
        sb.append("    cluster: ").append(toIndentedString(cluster)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("    operatingAt: ").append(toIndentedString(operatingSystem)).append("\n");
        sb.append("    private ips: ").append(toIndentedString(privateIpAddresses)).append("\n");
        sb.append("    public ips: ").append(toIndentedString(publicIpAddresses)).append("\n");
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
