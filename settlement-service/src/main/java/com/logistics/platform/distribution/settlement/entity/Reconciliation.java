package com.logistics.platform.distribution.settlement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reconciliation")
@Data
public class Reconciliation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reconciliation_no", unique = true, nullable = false, length = 64)
    private String reconciliationNo;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_orders")
    private Integer totalOrders;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "reconciliation_status")
    private String reconciliationStatus = "PENDING";

    @Column(name = "checker_id")
    private Long checkerId;

    @Column(name = "check_time")
    private LocalDateTime checkTime;

    @Column(name = "remark")
    private String remark;

    @Column(name = "create_time")
    private LocalDateTime createTime = LocalDateTime.now();
}