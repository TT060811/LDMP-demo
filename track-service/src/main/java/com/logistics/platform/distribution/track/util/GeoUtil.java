package com.logistics.platform.distribution.track.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 地理计算工具类（距离、方向、角度）
 */
@Slf4j
public class GeoUtil {
    // 地球半径（米）
    private static final double EARTH_RADIUS = 6371000;

    /**
     * 计算两点间距离（米）
     */
    public static BigDecimal calculateDistance(BigDecimal lng1, BigDecimal lat1, BigDecimal lng2, BigDecimal lat2) {
        double radLat1 = Math.toRadians(lat1.doubleValue());
        double radLat2 = Math.toRadians(lat2.doubleValue());
        double a = radLat1 - radLat2;
        double b = Math.toRadians(lng1.doubleValue()) - Math.toRadians(lng2.doubleValue());

        double s = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(a / 2), 2) +
                        Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)
        ));
        s = s * EARTH_RADIUS;
        return BigDecimal.valueOf(s).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 计算移动方向（东/南/西/北/东北等）
     */
    public static String getDirection(BigDecimal lng1, BigDecimal lat1, BigDecimal lng2, BigDecimal lat2) {
        double lngDiff = lng2.subtract(lng1).doubleValue();
        double latDiff = lat2.subtract(lat1).doubleValue();

        if (latDiff > 0 && lngDiff > 0) return "东北";
        if (latDiff > 0 && lngDiff < 0) return "西北";
        if (latDiff < 0 && lngDiff > 0) return "东南";
        if (latDiff < 0 && lngDiff < 0) return "西南";
        if (latDiff > 0) return "北";
        if (latDiff < 0) return "南";
        if (lngDiff > 0) return "东";
        return "西";
    }

    /**
     * 判断是否为高速（简化版：速度>60km/h判定为高速）
     */
    public static boolean isHighway(BigDecimal lng1, BigDecimal lat1, BigDecimal lng2, BigDecimal lat2, int time) {
        BigDecimal distance = calculateDistance(lng1, lat1, lng2, lat2); // 米
        double speed = (distance.doubleValue() / 1000) / (time / 3600); // km/h
        return speed > 60;
    }
}