package com.logistics.platform.distribution.track.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * GPS轨迹实体（对应数据库gps_track表）
 */
@Data
@Entity
@Table(name = "gps_track")
public class GpsTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "delivery_task_id", nullable = false)
    private Long deliveryTaskId; // 配送任务ID

    @Column(name = "longitude", precision = 10, scale = 6, nullable = false)
    private BigDecimal longitude; // 经度

    @Column(name = "latitude", precision = 10, scale = 6, nullable = false)
    private BigDecimal latitude; // 纬度

    @Column(name = "speed", precision = 3, scale = 1)
    private BigDecimal speed; // 速度（km/h）

    @Column(name = "direction")
    private String direction; // 方向（东/南/西/北/东北等）

    @Column(name = "track_time", nullable = false)
    private LocalDateTime trackTime; // 轨迹时间

    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createTime; // 入库时间
}