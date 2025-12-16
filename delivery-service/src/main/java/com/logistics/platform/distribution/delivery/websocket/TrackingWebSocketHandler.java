package com.logistics.platform.distribution.delivery.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class TrackingWebSocketHandler extends TextWebSocketHandler {

    // 存储所有连接会话
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        sessions.put(sessionId, session);

        log.info("WebSocket连接建立: {}", sessionId);

        // 发送欢迎消息
        Map<String, Object> welcomeMsg = Map.of(
                "type", "CONNECTED",
                "sessionId", sessionId,
                "message", "配送轨迹推送服务已连接",
                "timestamp", System.currentTimeMillis()
        );
        sendMessage(session, welcomeMsg);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.debug("收到WebSocket消息: {}", payload);

        try {
            Map<String, Object> data = objectMapper.readValue(payload, Map.class);
            String type = (String) data.get("type");

            switch (type) {
                case "SUBSCRIBE":
                    handleSubscribe(session, data);
                    break;
                case "LOCATION_REPORT":
                    handleLocationReport(session, data);
                    break;
                default:
                    log.warn("未知的消息类型: {}", type);
            }
        } catch (Exception e) {
            log.error("处理WebSocket消息失败", e);
            Map<String, Object> errorMsg = Map.of(
                    "type", "ERROR",
                    "message", "消息格式错误",
                    "timestamp", System.currentTimeMillis()
            );
            sendMessage(session, errorMsg);
        }
    }

    private void handleSubscribe(WebSocketSession session, Map<String, Object> data) {
        String taskId = (String) data.get("taskId");
        log.info("客户端订阅配送任务轨迹: taskId={}", taskId);

        // 存储订阅关系（实际项目中可能需要更复杂的管理）
        session.getAttributes().put("subscribedTaskId", taskId);

        Map<String, Object> response = Map.of(
                "type", "SUBSCRIBED",
                "taskId", taskId,
                "message", "成功订阅配送任务轨迹",
                "timestamp", System.currentTimeMillis()
        );
        sendMessage(session, response);
    }

    private void handleLocationReport(WebSocketSession session, Map<String, Object> data) {
        String taskId = (String) data.get("taskId");
        Double latitude = (Double) data.get("latitude");
        Double longitude = (Double) data.get("longitude");

        log.debug("配送任务 {} 位置报告: ({}, {})", taskId, latitude, longitude);

        // 构建位置更新消息
        Map<String, Object> locationMsg = Map.of(
                "type", "LOCATION_UPDATE",
                "taskId", taskId,
                "latitude", latitude,
                "longitude", longitude,
                "timestamp", System.currentTimeMillis()
        );

        // 广播给所有客户端（实际应该只发送给订阅了该任务的客户端）
        broadcastMessage(locationMsg);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        sessions.remove(sessionId);
        log.info("WebSocket连接关闭: {}, 状态: {}", sessionId, status);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket传输错误", exception);
    }

    // 发送消息给指定会话
    public void sendMessage(WebSocketSession session, Object message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            log.error("发送WebSocket消息失败", e);
        }
    }

    // 广播消息给所有连接的客户端
    public void broadcastMessage(Object message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            TextMessage textMessage = new TextMessage(json);

            for (WebSocketSession session : sessions.values()) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(textMessage);
                    } catch (IOException e) {
                        log.error("广播消息失败", e);
                    }
                }
            }
        } catch (IOException e) {
            log.error("序列化广播消息失败", e);
        }
    }

    // 获取当前连接数
    public int getConnectionCount() {
        return sessions.size();
    }
}