package com.logistics.platform.distribution.track.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 高德地图API工具类（路径规划、地理编码）
 */
@Slf4j
@Component
public class AmapUtil {
    @Value("${amap.key}")
    private String amapKey;

    @Value("${amap.direction-url}")
    private String directionUrl;

    /**
     * 地址转经纬度（地理编码）
     * @param addr 地址
     * @return 经纬度数组 [lng, lat]
     */
    public Double[] geocode(String addr) {
        String url = "https://restapi.amap.com/v3/geocode/geo";
        Map<String, Object> params = new HashMap<>();
        params.put("key", amapKey);
        params.put("address", addr);
        params.put("city", "北京市"); // 限定城市，提高精度

        String result = HttpUtil.get(url, params);
        JSONObject json = JSON.parseObject(result);
        if (!"1".equals(json.getString("status"))) {
            log.error("地理编码失败：{}", json.getString("info"));
            throw new RuntimeException("地址解析失败：" + addr);
        }

        List<JSONObject> geocodes = json.getJSONArray("geocodes").toList(JSONObject.class);
        if (geocodes.isEmpty()) {
            throw new RuntimeException("未找到该地址的经纬度：" + addr);
        }

        String lnglat = geocodes.get(0).getString("location");
        String[] split = lnglat.split(",");
        return new Double[]{Double.parseDouble(split[0]), Double.parseDouble(split[1])};
    }

    /**
     * 驾车路径规划（获取真实道路节点）
     * @param startLnglat 起点经纬度 [lng, lat]
     * @param endLnglat 终点经纬度 [lng, lat]
     * @return 道路节点列表 [[lng1, lat1], [lng2, lat2], ...]
     */
    public List<Double[]> drivingRoute(Double[] startLnglat, Double[] endLnglat) {
        String url = directionUrl;
        Map<String, Object> params = new HashMap<>();
        params.put("key", amapKey);
        params.put("origin", startLnglat[0] + "," + startLnglat[1]);
        params.put("destination", endLnglat[0] + "," + endLnglat[1]);
        params.put("strategy", "0"); // 0=最短时间策略

        String result = HttpUtil.get(url, params);
        JSONObject json = JSON.parseObject(result);
        if (!"1".equals(json.getString("status"))) {
            log.error("路径规划失败：{}", json.getString("info"));
            throw new RuntimeException("路径规划失败：" + json.getString("info"));
        }

        // 提取路径节点
        JSONObject route = json.getJSONArray("routes").getJSONObject(0);
        JSONObject path = route.getJSONArray("paths").getJSONObject(0);
        String pathJson = path.getString("path");
        String[] points = pathJson.split(";");

        return cn.hutool.core.collection.CollectionUtil.toList(points).stream()
                .map(point -> {
                    String[] split = point.split(",");
                    return new Double[]{Double.parseDouble(split[0]), Double.parseDouble(split[1])};
                }).toList();
    }
}