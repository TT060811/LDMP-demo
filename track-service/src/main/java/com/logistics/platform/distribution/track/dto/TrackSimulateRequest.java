package com.logistics.platform.distribution.track.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 模拟轨迹生成请求
 */
@Data
public class TrackSimulateRequest {
    @NotNull(message = "配送任务ID不能为空")
    private Long deliveryTaskId; // 配送任务ID

    @NotBlank(message = "起点地址不能为空")
    private String startAddr; // 起点地址（如：北京市朝阳区望京SOHO）

    @NotBlank(message = "终点地址不能为空")
    private String endAddr; // 终点地址（如：北京市海淀区中关村）

    private Integer totalPoints = 200; // 轨迹总点数（默认200）
}