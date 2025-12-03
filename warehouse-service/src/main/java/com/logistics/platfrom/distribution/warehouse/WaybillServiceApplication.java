// WaybillServiceApplication.java
package com.logistics.platform.distribution.waybill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // 开启Nacos服务注册（低版本Spring Cloud需加）
public class WaybillServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WaybillServiceApplication.class, args);
    }
}