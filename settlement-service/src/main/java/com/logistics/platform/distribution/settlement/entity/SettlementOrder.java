package com.logistics.platform.distribution.settlement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "settlement_order")
@Data
public class SettlementOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "settlement_no", unique = true, nullable = false, length = 64)
    private String settlementNo;

    @Column(name = "waybill_id", nullable = false)
    private Long waybillId;

    @Column(name = "deliveryman_id", nullable = false)
    private Long deliverymanId;

    @Column(name = "base_fee")
    private BigDecimal baseFee;

    @Column(name = "extra_fee")
    private BigDecimal extraFee = BigDecimal.ZERO;

    @Column(name = "total_fee")
    private BigDecimal totalFee;

    @Column(name = "payment_status")
    private String paymentStatus = "UNPAID";

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "settlement_status")
    private String settlementStatus = "PENDING";

    @Column(name = "settlement_time")
    private LocalDateTime settlementTime;

    @Column(name = "cancel_time")  // 添加这个字段
    private LocalDateTime cancelTime;

    @Column(name = "remark")
    private String remark;

    @Column(name = "create_time")
    private LocalDateTime createTime = LocalDateTime.now();
}