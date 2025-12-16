package com.logistics.platform.distribution.track.controller;

import com.logistics.platform.distribution.track.dto.GpsTrackDTO;
import com.logistics.platform.distribution.track.dto.TrackAnalysisDTO;
import com.logistics.platform.distribution.track.dto.TrackSimulateRequest;
import com.logistics.platform.distribution.track.entity.GpsTrack;
import com.logistics.platform.distribution.track.entity.TrackAnalysis;
import com.logistics.platform.distribution.track.repository.GpsTrackRepository;
import com.logistics.platform.distribution.track.repository.TrackAnalysisRepository;
import com.logistics.platform.distribution.track.service.TrackAnalysisService; // 注入接口
import com.logistics.platform.distribution.track.service.TrackSimulateService;   // 注入接口
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/track")
public class TrackController {

    // 注入接口（而非Impl实现类）
    @Resource
    private TrackSimulateService trackSimulateService;
    @Resource
    private TrackAnalysisService trackAnalysisService;

    @Resource
    private GpsTrackRepository gpsTrackRepository;
    @Resource
    private TrackAnalysisRepository trackAnalysisRepository;

    // 其余接口逻辑不变，和之前一致
    @PostMapping("/simulate/real-road")
    public ResponseEntity<List<GpsTrackDTO>> generateRealRoadTrack(@Valid @RequestBody TrackSimulateRequest request) {
        List<GpsTrack> trackList = trackSimulateService.generateRealRoadTrack(request);
        List<GpsTrackDTO> dtoList = trackList.stream()
                .map(track -> {
                    GpsTrackDTO dto = new GpsTrackDTO();
                    BeanUtils.copyProperties(track, dto);
                    return dto;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }


    /**
     * 分析轨迹
     */
    @PostMapping("/analyze/{taskId}")
    public ResponseEntity<TrackAnalysisDTO> analyzeTrack(@PathVariable Long taskId) {
        TrackAnalysis analysis = trackAnalysisService.analyzeTrack(taskId);
        TrackAnalysisDTO dto = new TrackAnalysisDTO();
        BeanUtils.copyProperties(analysis, dto);
        return ResponseEntity.ok(dto);
    }

    /**
     * 查询轨迹分析结果
     */
    @GetMapping("/analyze/{taskId}")
    public ResponseEntity<TrackAnalysisDTO> getTrackAnalysis(@PathVariable Long taskId) {
        Optional<TrackAnalysis> optional = trackAnalysisRepository.findTopByDeliveryTaskIdOrderByAnalysisTimeDesc(taskId);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        TrackAnalysisDTO dto = new TrackAnalysisDTO();
        BeanUtils.copyProperties(optional.get(), dto);
        return ResponseEntity.ok(dto);
    }
}