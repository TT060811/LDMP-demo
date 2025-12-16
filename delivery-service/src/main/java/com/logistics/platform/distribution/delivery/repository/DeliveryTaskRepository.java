package com.logistics.platform.distribution.delivery.repository;


import com.logistics.platform.distribution.delivery.entity.DeliveryTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface DeliveryTaskRepository extends JpaRepository<DeliveryTask, Long> {
    Optional<DeliveryTask> findByTaskNo(String taskNo);
    List<DeliveryTask> findByDeliverymanId(Long deliverymanId);
    List<DeliveryTask> findByStatus(String status);
    List<DeliveryTask> findByWaybillId(Long waybillId);
    List<DeliveryTask> findByWarehouseId(Long warehouseId);
    Optional<DeliveryTask> findByWaybillIdAndStatus(Long waybillId, String status);
    Page<DeliveryTask> findByDeliverymanId(Long deliverymanId, Pageable pageable);
    Page<DeliveryTask> findByStatus(String status, Pageable pageable);
    Page<DeliveryTask> findByWaybillId(Long waybillId, Pageable pageable);
    Page<DeliveryTask> findByWarehouseId(Long warehouseId, Pageable pageable);
}
