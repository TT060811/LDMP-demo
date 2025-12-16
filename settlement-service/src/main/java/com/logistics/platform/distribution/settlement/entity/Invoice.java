package com.logistics.platform.distribution.settlement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice")
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_no", unique = true, nullable = false)
    private String invoiceNo;

    @Column(name = "settlement_id", nullable = false)
    private Long settlementId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "invoice_type")
    private String invoiceType = "ELECTRONIC";

    @Column(name = "invoice_amount")
    private BigDecimal invoiceAmount;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "status")
    private String status = "CREATED";

    @Column(name = "issue_time")
    private LocalDateTime issueTime;

    @Column(name = "download_url", length = 500)
    private String downloadUrl;

    @Column(name = "create_time")
    private LocalDateTime createTime = LocalDateTime.now();
}