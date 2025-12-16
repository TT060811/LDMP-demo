package com.logistics.platform.distribution.delivery.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "deliveryman")
@Data
public class Deliveryman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "员工工号不能为空")
    @Column(name = "employee_no", unique = true, nullable = false, length = 50)
    private String employeeNo;

    @NotBlank(message = "姓名不能为空")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "id_card", length = 50)
    private String idCard;

    @Column(name = "status")
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, ON_LEAVE

    @Column(name = "current_latitude", precision = 10, scale = 6)
    private BigDecimal currentLatitude;

    @Column(name = "current_longitude", precision = 10, scale = 6)
    private BigDecimal currentLongitude;

    @Column(name = "current_order_id")
    private Long currentOrderId;

    @Column(name = "total_orders")
    private Integer totalOrders = 0;

    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.valueOf(5.00);

    @Column(name = "create_time")
    private LocalDateTime createTime;
}