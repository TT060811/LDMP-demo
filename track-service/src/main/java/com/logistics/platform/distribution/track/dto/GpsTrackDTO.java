package com.logistics.platform.distribution.track.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * GPS轨迹返回DTO
 */
@Data
public class GpsTrackDTO {
    private Long id;
    private Long deliveryTaskId;
    private BigDecimal longitude; // 经度
    private BigDecimal latitude; // 纬度
    private BigDecimal speed; // 速度
    private String direction; // 方向
    private LocalDateTime trackTime; // 轨迹时间
}