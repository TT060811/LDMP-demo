package com.logistics.platform.distribution.delivery.entity;

public enum DeliveryStatus {
    ASSIGNED("已分配"),
    PICKED_UP("已取货"),
    DELIVERING("配送中"),
    DELIVERED("已送达"),
    CANCELLED("已取消");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static DeliveryStatus fromString(String status) {
        if (status == null) {
            return null;
        }
        for (DeliveryStatus ds : DeliveryStatus.values()) {
            if (ds.name().equals(status)) {
                return ds;
            }
        }
        throw new IllegalArgumentException("未知的配送状态: " + status);
    }
}