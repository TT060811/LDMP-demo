package com.logistics.platfrom.distribution.warehouse.repository;

import com.logistics.platfrom.distribution.warehouse.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    // 按仓库编码查询（避免重复创建）
    Warehouse findByWarehouseCode(String warehouseCode);
}