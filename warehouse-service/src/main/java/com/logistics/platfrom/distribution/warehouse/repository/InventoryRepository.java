package com.logistics.platfrom.distribution.warehouse.repository;
import com.logistics.platfrom.distribution.warehouse.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    // 按仓库ID查询库存（常用）
    List<Inventory> findByWarehouseId(Long warehouseId);

    // 按运单ID查询（出库时用）
    List<Inventory> findByWaybillId(Long waybillId);
}