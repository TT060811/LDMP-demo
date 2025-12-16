package com.logistics.platfrom.distribution.warehouse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Objects;

/**
 * 库存实体类
 */
@Entity
@Table(name = "reconciliation")
@Data
public class Inventory {

    @Id
    private Long id;
    private Long waybillId;        // 关联运单ID
    private Long warehouseId;      // 所属仓库ID
    private Long locationId;       // 所在库位ID
    private String goodsName;      // 货物名称
    private Integer quantity;      // 数量
    private String unit;           // 单位
    private String status;         // 状态 (例如: INBOUND, OUTBOUND)

    // --- Getters ---

    public Long getId() {
        return id;
    }

    public Long getWaybillId() {
        return waybillId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getStatus() {
        return status;
    }

    // --- Setters ---

    public void setId(Long id) {
        this.id = id;
    }

    public void setWaybillId(Long waybillId) {
        this.waybillId = waybillId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // --- Optional: equals, hashCode, toString ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventory inventory = (Inventory) o;
        return Objects.equals(id, inventory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", waybillId=" + waybillId +
                ", warehouseId=" + warehouseId +
                ", locationId=" + locationId +
                ", goodsName='" + goodsName + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}