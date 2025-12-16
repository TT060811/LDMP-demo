package com.logistics.platform.distribution.track.service.impl;

import com.logistics.platform.distribution.track.entity.GpsTrack;
import com.logistics.platform.distribution.track.entity.TrackAnalysis;
import com.logistics.platform.distribution.track.repository.GpsTrackRepository;
import com.logistics.platform.distribution.track.repository.TrackAnalysisRepository;
import com.logistics.platform.distribution.track.service.TrackAnalysisService;
import com.logistics.platform.distribution.track.util.GeoUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 轨迹分析服务实现类
 */
@Slf4j
@Service
public class TrackAnalysisServiceImpl implements TrackAnalysisService {

    @Resource
    private GpsTrackRepository gpsTrackRepository;
    @Resource
    private TrackAnalysisRepository trackAnalysisRepository;

    /**
     * 分析指定配送任务的轨迹（核心实现）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TrackAnalysis analyzeTrack(Long deliveryTaskId) {
        // 1. 查询轨迹数据
        List<GpsTrack> trackList = gpsTrackRepository.findByDeliveryTaskIdOrderByTrackTimeAsc(deliveryTaskId);
        if (trackList.isEmpty()) {
            throw new RuntimeException("未找到该任务的轨迹数据：" + deliveryTaskId);
        }

        // 2. 计算总距离
        BigDecimal totalDistance = BigDecimal.ZERO;
        for (int i = 0; i < trackList.size() - 1; i++) {
            GpsTrack t1 = trackList.get(i);
            GpsTrack t2 = trackList.get(i + 1);
            totalDistance = totalDistance.add(GeoUtil.calculateDistance(
                    t1.getLongitude(), t1.getLatitude(),
                    t2.getLongitude(), t2.getLatitude()
            ));
        }

        // 3. 计算总时长（秒）
        LocalDateTime startTime = trackList.get(0).getTrackTime();
        LocalDateTime endTime = trackList.get(trackList.size() - 1).getTrackTime();
        int totalTime = (int) Duration.between(startTime, endTime).getSeconds();

        // 4. 计算平均速度（km/h）
        BigDecimal avgSpeed = BigDecimal.ZERO;
        if (totalTime > 0) {
            avgSpeed = (totalDistance.divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP))
                    .divide(BigDecimal.valueOf(totalTime / 3600), 1, RoundingMode.HALF_UP);
        }

        // 5. 计算最高速度
        BigDecimal maxSpeed = trackList.stream()
                .map(GpsTrack::getSpeed)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        // 6. 计算停留次数（速度=0的点）
        int stopsCount = (int) trackList.stream()
                .filter(track -> track.getSpeed().compareTo(BigDecimal.ZERO) == 0)
                .count();

        // 7. 保存分析结果
        TrackAnalysis analysis = new TrackAnalysis();
        analysis.setDeliveryTaskId(deliveryTaskId);
        analysis.setTotalDistance(totalDistance);
        analysis.setTotalTime(totalTime);
        analysis.setAvgSpeed(avgSpeed);
        analysis.setMaxSpeed(maxSpeed);
        analysis.setStopsCount(stopsCount);
        analysis.setAnalysisTime(LocalDateTime.now());

        TrackAnalysis saved = trackAnalysisRepository.save(analysis);
        log.info("轨迹分析完成：任务ID={}，总距离={}米，平均速度={}km/h",
                deliveryTaskId, totalDistance, avgSpeed);

        return saved;
    }

    /**
     * 批量分析轨迹（扩展接口实现）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchAnalyzeTrack(List<Long> taskIds) {
        int count = 0;
        for (Long taskId : taskIds) {
            try {
                analyzeTrack(taskId);
                count++;
            } catch (Exception e) {
                log.error("批量分析轨迹失败：任务ID={}，原因={}", taskId, e.getMessage());
            }
        }
        log.info("批量轨迹分析完成：成功{}个，失败{}个", count, taskIds.size() - count);
        return count;
    }
}