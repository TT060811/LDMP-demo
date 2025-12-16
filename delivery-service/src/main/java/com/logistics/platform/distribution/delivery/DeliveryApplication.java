package com.logistics.platform.distribution.delivery;

import com.logistics.platform.distribution.delivery.entity.Deliveryman;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
        System.out.println("==========================================");
        System.out.println("配送服务启动成功!");
        System.out.println("WebSocket连接地址: ws://localhost:8081/ws/delivery-tracking");
        System.out.println("REST API地址: http://localhost:8081/api/delivery");
        System.out.println("==========================================");
    }
}
