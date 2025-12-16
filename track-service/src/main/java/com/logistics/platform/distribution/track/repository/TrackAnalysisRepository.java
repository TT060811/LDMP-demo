package com.logistics.platform.distribution.track.repository;

import com.logistics.platform.distribution.track.entity.TrackAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 轨迹分析结果数据层
 */
public interface TrackAnalysisRepository extends JpaRepository<TrackAnalysis, Long> {
    /**
     * 按配送任务ID查询最新分析结果
     */
    Optional<TrackAnalysis> findTopByDeliveryTaskIdOrderByAnalysisTimeDesc(Long deliveryTaskId);
}