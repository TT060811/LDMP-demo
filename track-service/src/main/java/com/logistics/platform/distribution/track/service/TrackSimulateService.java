package com.logistics.platform.distribution.track.service;

import com.logistics.platform.distribution.track.dto.TrackSimulateRequest;
import com.logistics.platform.distribution.track.entity.GpsTrack;

import java.util.List;

/**
 * 模拟轨迹生成服务接口
 */
public interface TrackSimulateService {

    /**
     * 生成真实道路的模拟轨迹
     * @param request 模拟轨迹请求参数
     * @return 生成的GPS轨迹列表
     */
    List<GpsTrack> generateRealRoadTrack(TrackSimulateRequest request);

    /**
     * 【扩展】生成简单直线模拟轨迹（备用，非真实道路）
     * @param taskId 配送任务ID
     * @param startLng 起点经度
     * @param startLat 起点纬度
     * @param endLng 终点经度
     * @param endLat 终点纬度
     * @param totalPoints 轨迹总点数
     * @return GPS轨迹列表
     */
    List<GpsTrack> generateSimpleTrack(Long taskId, Double startLng, Double startLat, Double endLng, Double endLat, Integer totalPoints);
}