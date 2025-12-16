package com.logistics.platform.distribution.delivery.controller;

import com.logistics.platform.distribution.delivery.entity.DeliveryTask;
import com.logistics.platform.distribution.delivery.repository.DeliveryTaskRepository;
import com.logistics.platform.distribution.delivery.websocket.TrackingWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
public class DeliveryTrackingController {

    private final DeliveryTaskRepository deliveryTaskRepository;
    private final TrackingWebSocketHandler trackingWebSocketHandler;

    // 获取配送员当前位置
    @GetMapping("/deliveryman/{deliverymanId}")
    public ResponseEntity<Map<String, Object>> getDeliverymanLocation(@PathVariable Long deliverymanId) {
        Map<String, Object> result = new HashMap<>();
        result.put("deliverymanId", deliverymanId);
        result.put("location", "模拟位置数据");
        result.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(result);
    }

    // 获取配送任务轨迹历史
    @GetMapping("/task/{taskId}/history")
    public ResponseEntity<Map<String, Object>> getTaskTrackingHistory(@PathVariable Long taskId) {
        Map<String, Object> result = new HashMap<>();
        result.put("taskId", taskId);
        result.put("locations", List.of(
                Map.of("lat", 31.2304, "lng", 121.4737, "time", "10:00"),
                Map.of("lat", 31.2314, "lng", 121.4747, "time", "10:15"),
                Map.of("lat", 31.2324, "lng", 121.4757, "time", "10:30")
        ));
        return ResponseEntity.ok(result);
    }

    // 手动触发位置更新（用于测试WebSocket）
    @PostMapping("/task/{taskId}/simulate-location")
    public ResponseEntity<Map<String, Object>> simulateLocationUpdate(@PathVariable Long taskId,
                                                                      @RequestParam Double lat,
                                                                      @RequestParam Double lng) {
        DeliveryTask task = deliveryTaskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("任务不存在"));

        // 构建位置更新消息
        Map<String, Object> locationMsg = new HashMap<>();
        locationMsg.put("type", "LOCATION_UPDATE");
        locationMsg.put("taskId", taskId.toString());
        locationMsg.put("taskNo", task.getTaskNo());
        locationMsg.put("latitude", lat);
        locationMsg.put("longitude", lng);
        locationMsg.put("timestamp", System.currentTimeMillis());

        // 广播给所有连接的客户端
        trackingWebSocketHandler.broadcastMessage(locationMsg);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "位置更新已发送");
        result.put("taskId", taskId);
        result.put("latitude", lat);
        result.put("longitude", lng);

        return ResponseEntity.ok(result);
    }
}