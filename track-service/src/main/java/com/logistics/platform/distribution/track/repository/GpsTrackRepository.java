package com.logistics.platform.distribution.track.repository;

import com.logistics.platform.distribution.track.entity.GpsTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GPS轨迹数据层
 */
public interface GpsTrackRepository extends JpaRepository<GpsTrack, Long> {
    /**
     * 按配送任务ID查询轨迹（按时间排序）
     */
    List<GpsTrack> findByDeliveryTaskIdOrderByTrackTimeAsc(Long deliveryTaskId);

    /**
     * 按配送任务ID和时间范围查询轨迹
     */
    @Query("SELECT t FROM GpsTrack t WHERE t.deliveryTaskId = :taskId AND t.trackTime BETWEEN :startTime AND :endTime ORDER BY t.trackTime ASC")
    List<GpsTrack> findByTaskIdAndTimeRange(
            @Param("taskId") Long taskId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}