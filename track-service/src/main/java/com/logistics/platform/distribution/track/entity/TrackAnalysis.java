package com.logistics.platform.distribution.track.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 轨迹分析结果实体（对应track_analysis表）
 */
@Data
@Entity
@Table(name = "track_analysis")
public class TrackAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "delivery_task_id", nullable = false)
    private Long deliveryTaskId; // 配送任务ID

    @Column(name = "total_distance", precision = 10, scale = 2)
    private BigDecimal totalDistance; // 总距离（米）

    @Column(name = "total_time")
    private Integer totalTime; // 总时长（秒）

    @Column(name = "avg_speed", precision = 3, scale = 1)
    private BigDecimal avgSpeed; // 平均速度（km/h）

    @Column(name = "max_speed", precision = 3, scale = 1)
    private BigDecimal maxSpeed; // 最高速度（km/h）

    @Column(name = "stops_count")
    private Integer stopsCount; // 停留次数

    @Column(name = "analysis_time")
    private LocalDateTime analysisTime; // 分析时间
}