package com.logistics.platfrom.distribution.warehouse.service.Impl; // 注意包名拼写，原图是 Impl

import com.logistics.platfrom.distribution.warehouse.common.Result;
import com.logistics.platfrom.distribution.warehouse.entity.Inventory;
import com.logistics.platfrom.distribution.warehouse.entity.StorageLocation;
import com.logistics.platfrom.distribution.warehouse.entity.Warehouse;
import com.logistics.platfrom.distribution.warehouse.feign.WaybillServiceFeignClient;
import com.logistics.platfrom.distribution.warehouse.repository.InventoryRepository;
import com.logistics.platfrom.distribution.warehouse.repository.StorageLocationRepository;
import com.logistics.platfrom.distribution.warehouse.repository.WarehouseRepository;
import com.logistics.platfrom.distribution.warehouse.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService { // 确保实现了接口

    private final WarehouseRepository warehouseRepository;
    private final StorageLocationRepository locationRepository;
    private final InventoryRepository inventoryRepository;
    private final WaybillServiceFeignClient waybillClient; // 依赖Feign客户端

    @Autowired
    public WarehouseServiceImpl(
            WarehouseRepository warehouseRepository,
            StorageLocationRepository locationRepository,
            InventoryRepository inventoryRepository,
            WaybillServiceFeignClient waybillClient) {
        this.warehouseRepository = warehouseRepository;
        this.locationRepository = locationRepository;
        this.inventoryRepository = inventoryRepository;
        this.waybillClient = waybillClient;
    }

    @Override
    public Warehouse createWarehouse(Warehouse warehouse) {
        warehouse.setStatus("ACTIVE"); // 现在有 setStatus 方法
        warehouse.setCreateTime(LocalDateTime.now()); // 现在有 setCreateTime 方法
        return warehouseRepository.save(warehouse);
    }

    @Override
    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    @Override
    public Inventory inboundInventory(Inventory inventory) { // 现在有 getWaybillId 方法
        // 1. 校验运单（通过Feign调用运单服务）
        Result<?> waybillResult = waybillClient.getWaybillById(inventory.getWaybillId()); // 现在有 getWaybillId 方法
        if (waybillResult.getCode() != 200) {
            throw new RuntimeException("运单不存在：" + inventory.getWaybillId());
        }

        // 2. 防超卖：Redis锁（实际项目用RedisTemplate）
        String lockKey = "inventory_lock:" + inventory.getWarehouseId() + ":" + inventory.getLocationId(); // 现在有 getWarehouseId, getLocationId
        if (!tryLock(lockKey)) {
            throw new RuntimeException("库存操作中，请稍后重试");
        }

        try {
            // 3. 保存库存
            Inventory savedInventory = inventoryRepository.save(inventory);

            // 4. 更新仓库容量
            Warehouse warehouse = warehouseRepository.findById(inventory.getWarehouseId()) // 现在有 getWarehouseId
                    .orElseThrow(() -> new RuntimeException("仓库不存在"));
            warehouse.setUsedCapacity(warehouse.getUsedCapacity() + inventory.getQuantity()); // 现在有 getQuantity, setUsedCapacity, getUsedCapacity
            warehouseRepository.save(warehouse);

            // 5. 更新库位状态
            StorageLocation location = locationRepository.findById(inventory.getLocationId()) // 现在有 getLocationId
                    .orElseThrow(() -> new RuntimeException("库位不存在"));
            location.setUsedCapacity(location.getUsedCapacity() + inventory.getQuantity()); // 现在有 setUsedCapacity, getUsedCapacity
            location.setStatus("OCCUPIED"); // 现在有 setStatus
            locationRepository.save(location);

            return savedInventory;
        } finally {
            unlock(lockKey);
        }
    }

    @Override
    public List<Inventory> getInventoryByWarehouseId(Long warehouseId) {
        return inventoryRepository.findByWarehouseId(warehouseId);
    }

    @Override
    public List<Inventory> getInventoryByWaybillId(Long waybillId) {
        return inventoryRepository.findByWaybillId(waybillId);
    }

    @Override
    public StorageLocation assignLocation(Long warehouseId, String locationCode) {
        // 1. 查找可用库位 (注意状态是 "EMPTY")
        StorageLocation location = locationRepository.findByWarehouseIdAndLocationCodeAndStatus(
                warehouseId, locationCode, "EMPTY"); // 现在有 getWarehouseId, getLocationCode (隐含在 findBy...)

        if (location == null) {
            throw new RuntimeException("库位不可用：" + locationCode);
        }

        // 2. 更新库位状态
        location.setStatus("OCCUPIED"); // 现在有 setStatus
        return locationRepository.save(location);
    }

    /**
     * 获取指定仓库下的所有可用库位
     * @param warehouseId 仓库ID
     * @return 可用库位列表
     */
    @Override
    public List<StorageLocation> getAvailableLocations(Long warehouseId) {
        // 假设可用库位的状态是 "EMPTY"
        return locationRepository.findByWarehouseIdAndStatus(warehouseId, "EMPTY");
    }


    // 🔥 Redis锁实现（实际项目用RedisTemplate）
    // TODO: 需要替换为真实的Redis加锁逻辑
    private boolean tryLock(String key) {
        // 示例（实际项目）：
        // Boolean absent = redisTemplate.opsForValue().setIfAbsent(key, "locked", Duration.ofSeconds(10));
        // return absent != null && absent;
        System.out.println("【模拟】尝试获取Redis锁: " + key); // 模拟日志
        return true; // 模拟成功（需替换为真实实现）
    }

    // TODO: 需要替换为真实的Redis解锁逻辑
    private void unlock(String key) {
        // 示例（实际项目）：
        // redisTemplate.delete(key);
        System.out.println("【模拟】释放Redis锁: " + key); // 模拟日志
        // 模拟空实现
    }
}