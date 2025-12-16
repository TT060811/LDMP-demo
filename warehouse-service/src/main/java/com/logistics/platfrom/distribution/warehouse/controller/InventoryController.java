package com.logistics.platfrom.distribution.warehouse.controller;


import com.logistics.platfrom.distribution.warehouse.common.Result;
import com.logistics.platfrom.distribution.warehouse.entity.Inventory;
import com.logistics.platfrom.distribution.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")

public class InventoryController {

    private final WarehouseService warehouseService;

    @Autowired
    public InventoryController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    // 入库操作（核心！）
    @PostMapping("/inbound")
    public Result<Inventory> inboundInventory(@RequestBody Inventory inventory) {
        Inventory result = warehouseService.inboundInventory(inventory);
        return Result.success(result);
    }

    // 查询仓库库存
    @GetMapping("/warehouse/{warehouseId}")
    public Result<List<Inventory>> getInventoryByWarehouseId(@PathVariable Long warehouseId) {
        List<Inventory> inventoryList = warehouseService.getInventoryByWarehouseId(warehouseId);
        return Result.success(inventoryList);
    }

    // 查询特定运单库存
    @GetMapping("/waybill/{waybillId}")
    public Result<List<Inventory>> getInventoryByWaybillId(@PathVariable Long waybillId) {
        // 实际业务中可能需要调用运单服务验证
        List<Inventory> inventoryList = warehouseService.getInventoryByWaybillId(waybillId);
        return Result.success(inventoryList);
    }
}