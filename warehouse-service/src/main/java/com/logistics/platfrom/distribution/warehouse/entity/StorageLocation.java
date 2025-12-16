package com.logistics.platfrom.distribution.warehouse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Objects;

@Entity
@Table(name = "reconciliation")
@Data
public class StorageLocation {

    @Id
    private Long id;
    private Long warehouseId;      // 所属仓库ID
    private String locationCode;   // 库位编码 (例如: A01-01)
    private Double capacity;       // 库位容量
    private Double usedCapacity;   // 已用容量
    private String status;         // 状态 (例如: EMPTY, OCCUPIED)

    // --- Getters ---

    public Long getId() {
        return id;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public Double getCapacity() {
        return capacity;
    }

    public Double getUsedCapacity() {
        return usedCapacity;
    }

    public String getStatus() {
        return status;
    }

    // --- Setters ---

    public void setId(Long id) {
        this.id = id;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public void setUsedCapacity(Double usedCapacity) {
        this.usedCapacity = usedCapacity;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // --- Optional: equals, hashCode, toString ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorageLocation that = (StorageLocation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StorageLocation{" +
                "id=" + id +
                ", warehouseId=" + warehouseId +
                ", locationCode='" + locationCode + '\'' +
                ", capacity=" + capacity +
                ", usedCapacity=" + usedCapacity +
                ", status='" + status + '\'' +
                '}';
    }
}