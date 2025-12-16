package com.logistics.platform.distribution.delivery.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_task")
@Data
public class DeliveryTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_no", unique = true, nullable = false, length = 64)
    private String taskNo;

    @Column(name = "waybill_id", nullable = false)
    private Long waybillId;

    @Column(name = "deliveryman_id", nullable = false)
    private Long deliverymanId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "status")
    private String status = "ASSIGNED"; // ASSIGNED, PICKED_UP, DELIVERING, DELIVERED, CANCELLED

    @Column(name = "assigned_time")
    private LocalDateTime assignedTime;

    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;

    @Column(name = "start_delivery_time")
    private LocalDateTime startDeliveryTime;

    @Column(name = "estimated_delivery_time")
    private LocalDateTime estimatedDeliveryTime;

    @Column(name = "actual_delivery_time")
    private LocalDateTime actualDeliveryTime;

    @Column(name = "delivery_fee", precision = 10, scale = 2)
    private BigDecimal deliveryFee;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}