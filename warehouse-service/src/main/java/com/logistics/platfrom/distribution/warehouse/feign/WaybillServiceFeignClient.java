package com.logistics.platfrom.distribution.warehouse.feign;

import com.logistics.platfrom.distribution.warehouse.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 调用运单服务的 Feign 客户端
 */
@FeignClient(name = "waybill-service") // 指定要调用的服务名称（与注册中心中的服务名一致）
public interface WaybillServiceFeignClient {

    @GetMapping("/api/waybill/{id}")
    Result<?> getWaybillById(@PathVariable("id") Long waybillId);
}