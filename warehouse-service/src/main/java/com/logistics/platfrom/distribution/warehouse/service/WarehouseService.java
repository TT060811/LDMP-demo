package com.logistics.platfrom.distribution.warehouse.service;


import com.logistics.platfrom.distribution.warehouse.entity.Inventory;
import com.logistics.platfrom.distribution.warehouse.entity.StorageLocation;
import com.logistics.platfrom.distribution.warehouse.entity.Warehouse;

import java.util.List;

public interface WarehouseService {
    // 仓库管理
    Warehouse createWarehouse(Warehouse warehouse);
    List<Warehouse> getAllWarehouses();

    // 库存管理
    Inventory inboundInventory(Inventory inventory);
    List<Inventory> getInventoryByWarehouseId(Long warehouseId);
    List<Inventory> getInventoryByWaybillId(Long waybillId);

    // 库位管理
    StorageLocation assignLocation(Long warehouseId, String locationCode);
    List<StorageLocation> getAvailableLocations(Long warehouseId);
}