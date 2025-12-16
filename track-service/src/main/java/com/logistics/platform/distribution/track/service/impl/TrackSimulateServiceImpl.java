package com.logistics.platform.distribution.track.service.impl;

import com.logistics.platform.distribution.track.dto.TrackSimulateRequest;
import com.logistics.platform.distribution.track.entity.GpsTrack;
import com.logistics.platform.distribution.track.repository.GpsTrackRepository;
import com.logistics.platform.distribution.track.service.TrackSimulateService;
import com.logistics.platform.distribution.track.util.AmapUtil;
import com.logistics.platform.distribution.track.util.GeoUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 模拟轨迹生成服务实现类
 */
@Slf4j
@Service // 标记为Spring服务Bean
public class TrackSimulateServiceImpl implements TrackSimulateService {

    @Resource
    private AmapUtil amapUtil;
    @Resource
    private GpsTrackRepository gpsTrackRepository;

    private static final Random RANDOM = new Random();

    /**
     * 生成真实道路的模拟轨迹（核心实现）
     */
    @Override
    @Transactional(rollbackFor = Exception.class) // 事务控制：异常回滚
    public List<GpsTrack> generateRealRoadTrack(TrackSimulateRequest request) {
        Long taskId = request.getDeliveryTaskId();
        int totalPointsPerSegment = request.getTotalPoints() / 20; // 每个道路节点间的插值点数

        // 步骤1：地址转经纬度
        Double[] startLnglat = amapUtil.geocode(request.getStartAddr());
        Double[] endLnglat = amapUtil.geocode(request.getEndAddr());

        // 步骤2：调用高德路径规划，获取真实道路节点
        List<Double[]> roadNodes = amapUtil.drivingRoute(startLnglat, endLnglat);
        if (roadNodes.isEmpty()) {
            throw new RuntimeException("未获取到有效道路节点");
        }

        // 步骤3：插值补点，生成密集轨迹
        List<GpsTrack> trackList = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now().minusMinutes(30); // 轨迹起始时间（30分钟前）

        for (int i = 0; i < roadNodes.size() - 1; i++) {
            Double[] node1 = roadNodes.get(i);
            Double[] node2 = roadNodes.get(i + 1);

            BigDecimal lng1 = BigDecimal.valueOf(node1[0]).setScale(6, RoundingMode.HALF_UP);
            BigDecimal lat1 = BigDecimal.valueOf(node1[1]).setScale(6, RoundingMode.HALF_UP);
            BigDecimal lng2 = BigDecimal.valueOf(node2[0]).setScale(6, RoundingMode.HALF_UP);
            BigDecimal lat2 = BigDecimal.valueOf(node2[1]).setScale(6, RoundingMode.HALF_UP);

            // 计算经纬度步长
            BigDecimal lngStep = lng2.subtract(lng1).divide(BigDecimal.valueOf(totalPointsPerSegment), 6, RoundingMode.HALF_UP);
            BigDecimal latStep = lat2.subtract(lat1).divide(BigDecimal.valueOf(totalPointsPerSegment), 6, RoundingMode.HALF_UP);

            // 生成该路段的轨迹点
            for (int j = 0; j < totalPointsPerSegment; j++) {
                GpsTrack track = new GpsTrack();
                track.setDeliveryTaskId(taskId);

                // 基础坐标（沿真实道路）+ 小幅随机偏移（±0.00005，约5米）
                BigDecimal currentLng = lng1.add(lngStep.multiply(BigDecimal.valueOf(j)))
                        .add(BigDecimal.valueOf(RANDOM.nextDouble() * 0.0001 - 0.00005));
                BigDecimal currentLat = lat1.add(latStep.multiply(BigDecimal.valueOf(j)))
                        .add(BigDecimal.valueOf(RANDOM.nextDouble() * 0.0001 - 0.00005));

                track.setLongitude(currentLng.setScale(6, RoundingMode.HALF_UP));
                track.setLatitude(currentLat.setScale(6, RoundingMode.HALF_UP));

                // 按道路类型适配速度（高速：60-80，市区：20-40）
                BigDecimal speed;
                if (GeoUtil.isHighway(lng1, lat1, lng2, lat2, totalPointsPerSegment * 5)) {
                    speed = BigDecimal.valueOf(60 + RANDOM.nextDouble() * 20).setScale(1, RoundingMode.HALF_UP);
                } else {
                    speed = BigDecimal.valueOf(20 + RANDOM.nextDouble() * 20).setScale(1, RoundingMode.HALF_UP);
                }
                track.setSpeed(speed);

                // 计算方向
                track.setDirection(GeoUtil.getDirection(lng1, lat1, currentLng, currentLat));

                // 时间戳（5秒间隔）
                track.setTrackTime(currentTime.plusSeconds(trackList.size() * 5));

                trackList.add(track);
            }
        }

        // 最后一个点强制对齐终点，速度为0
        GpsTrack lastTrack = trackList.get(trackList.size() - 1);
        lastTrack.setLongitude(BigDecimal.valueOf(endLnglat[0]).setScale(6, RoundingMode.HALF_UP));
        lastTrack.setLatitude(BigDecimal.valueOf(endLnglat[1]).setScale(6, RoundingMode.HALF_UP));
        lastTrack.setSpeed(BigDecimal.ZERO);
        lastTrack.setDirection("停止");

        // 步骤4：批量保存到数据库
        List<GpsTrack> savedTracks = gpsTrackRepository.saveAll(trackList);
        log.info("真实道路模拟轨迹生成完成：任务ID={}，轨迹点数={}", taskId, savedTracks.size());

        return savedTracks;
    }

    /**
     * 生成简单直线模拟轨迹（非真实道路，备用）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<GpsTrack> generateSimpleTrack(Long taskId, Double startLng, Double startLat, Double endLng, Double endLat, Integer totalPoints) {
        List<GpsTrack> trackList = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now().minusMinutes(30);

        // 计算步长
        BigDecimal lngStep = BigDecimal.valueOf((endLng - startLng) / totalPoints).setScale(6, RoundingMode.HALF_UP);
        BigDecimal latStep = BigDecimal.valueOf((endLat - startLat) / totalPoints).setScale(6, RoundingMode.HALF_UP);

        for (int i = 0; i < totalPoints; i++) {
            GpsTrack track = new GpsTrack();
            track.setDeliveryTaskId(taskId);
            track.setLongitude(BigDecimal.valueOf(startLng).add(lngStep.multiply(BigDecimal.valueOf(i)))
                    .add(BigDecimal.valueOf(RANDOM.nextDouble() * 0.0001 - 0.00005)));
            track.setLatitude(BigDecimal.valueOf(startLat).add(latStep.multiply(BigDecimal.valueOf(i)))
                    .add(BigDecimal.valueOf(RANDOM.nextDouble() * 0.0001 - 0.00005)));
            track.setSpeed(BigDecimal.valueOf(30 + RANDOM.nextDouble() * 30).setScale(1, RoundingMode.HALF_UP));
            track.setDirection(GeoUtil.getDirection(
                    BigDecimal.valueOf(startLng), BigDecimal.valueOf(startLat),
                    track.getLongitude(), track.getLatitude()
            ));
            track.setTrackTime(currentTime.plusSeconds(i * 5));
            trackList.add(track);
        }

        // 最后一个点对齐终点
        GpsTrack lastTrack = trackList.get(trackList.size() - 1);
        lastTrack.setLongitude(BigDecimal.valueOf(endLng));
        lastTrack.setLatitude(BigDecimal.valueOf(endLat));
        lastTrack.setSpeed(BigDecimal.ZERO);
        lastTrack.setDirection("停止");

        return gpsTrackRepository.saveAll(trackList);
    }
}