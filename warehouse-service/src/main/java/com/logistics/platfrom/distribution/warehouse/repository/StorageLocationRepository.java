package com.logistics.platfrom.distribution.warehouse.repository;

import com.logistics.platfrom.distribution.warehouse.entity.StorageLocation;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface StorageLocationRepository extends JpaRepository<StorageLocation, Long> {
    // 用于 assignLocation 方法
    StorageLocation findByWarehouseIdAndLocationCodeAndStatus(
            Long warehouseId, String locationCode, String status);

    // 用于 getAvailableLocations 方法
    List<StorageLocation> findByWarehouseIdAndStatus(Long warehouseId, String status);
}