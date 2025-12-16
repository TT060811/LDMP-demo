package com.logistics.platform.distribution.track.service;

import com.logistics.platform.distribution.track.entity.TrackAnalysis;

import java.util.List;

/**
 * 轨迹分析服务接口
 */
public interface TrackAnalysisService {

    /**
     * 分析指定配送任务的轨迹
     * @param deliveryTaskId 配送任务ID
     * @return 轨迹分析结果
     */
    TrackAnalysis analyzeTrack(Long deliveryTaskId);

    /**
     * 批量分析轨迹（扩展接口）
     * @param taskIds 配送任务ID列表
     * @return 分析结果数量
     */
    Integer batchAnalyzeTrack(List<Long> taskIds);
}