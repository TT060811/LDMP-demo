package com.logistics.platform.distribution.track.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 轨迹分析结果DTO
 */
@Data
public class TrackAnalysisDTO {
    private Long id;
    private Long deliveryTaskId;
    private BigDecimal totalDistance; // 总距离（米）
    private Integer totalTime; // 总时长（秒）
    private BigDecimal avgSpeed; // 平均速度（km/h）
    private BigDecimal maxSpeed; // 最高速度（km/h）
    private Integer stopsCount; // 停留次数
    private LocalDateTime analysisTime; // 分析时间
}