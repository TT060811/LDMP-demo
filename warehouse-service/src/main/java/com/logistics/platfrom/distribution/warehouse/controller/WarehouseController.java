package com.logistics.platfrom.distribution.warehouse.controller;


import com.logistics.platfrom.distribution.warehouse.common.Result;
import com.logistics.platfrom.distribution.warehouse.entity.Warehouse;
import com.logistics.platfrom.distribution.warehouse.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    // 创建仓库
    @PostMapping
    public Result<Warehouse> createWarehouse(@RequestBody Warehouse warehouse) {
        Warehouse result = warehouseService.createWarehouse(warehouse);
        return Result.success(result);
    }

    // 获取所有仓库
    @GetMapping
    public Result<List<Warehouse>> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseService.getAllWarehouses();
        return Result.success(warehouses);
    }

    // 分配库位（自动分配可用库位）
    @PostMapping("/assign-location")
    public Result<?> assignLocation(@RequestParam Long warehouseId, @RequestParam String locationCode) {
        warehouseService.assignLocation(warehouseId, locationCode);
        return Result.success(null);
    }
}