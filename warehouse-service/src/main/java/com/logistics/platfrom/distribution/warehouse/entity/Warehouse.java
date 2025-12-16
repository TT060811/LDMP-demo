package com.logistics.platfrom.distribution.warehouse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 仓库实体类
 */
@Getter
@Entity
@Table(name = "reconciliation")
@Data
public class Warehouse {

    @Id
    private Long id;
    private String warehouseCode;  // 仓库编码
    private String name;           // 仓库名称
    private String address;        // 仓库地址
    private Double capacity;       // 总容量
    private Double usedCapacity;   // 已用容量
    private String status;         // 状态 (ACTIVE, INACTIVE)
    private LocalDateTime createTime; // 创建时间

    // --- Getters ---


    // --- Setters ---

    public void setId(Long id) {
        this.id = id;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }


    // --- Optional: equals, hashCode, toString ---
    // 为了完整性，这里也加上，虽然可能由 Lombok 自动生成

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return Objects.equals(id, warehouse.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "id=" + id +
                ", warehouseCode='" + warehouseCode + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", capacity=" + capacity +
                ", usedCapacity=" + usedCapacity +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}