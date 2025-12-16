package com.logistics.platform.distribution.delivery.service;

import com.logistics.platform.distribution.delivery.entity.DeliveryTask;
import com.logistics.platform.distribution.delivery.entity.Deliveryman;

import java.math.BigDecimal;
import java.util.List;

public interface DeliveryService {

    // 配送员
    Deliveryman createDeliveryman(Deliveryman deliveryman);
    List<Deliveryman> getAllDeliverymen();
    Deliveryman getDeliverymanById(Long id);
    Deliveryman updateDeliveryman(Long id, Deliveryman deliveryman);
    void deleteDeliveryman(Long id);
    Deliveryman updateDeliverymanLocation(Long id, BigDecimal latitude, BigDecimal longitude);
    List<Deliveryman> getAvailableDeliverymen();

    // 配送任务
    DeliveryTask createDeliveryTask(DeliveryTask deliveryTask);
    List<DeliveryTask> getAllDeliveryTasks();
    DeliveryTask getDeliveryTaskById(Long id);
    DeliveryTask getDeliveryTaskByNo(String taskNo);
    DeliveryTask assignDeliveryTask(Long taskId, Long deliverymanId);
    DeliveryTask updateTaskStatus(Long taskId, String status);
    DeliveryTask pickUpTask(Long taskId);
    DeliveryTask startDelivery(Long taskId);
    DeliveryTask completeDelivery(Long taskId);
    DeliveryTask cancelDeliveryTask(Long taskId);

    //查询
    List<DeliveryTask> getTasksByDeliveryman(Long deliverymanId);
    List<DeliveryTask> getTasksByStatus(String status);
    List<DeliveryTask> getTasksByWaybill(Long waybillId);
    List<DeliveryTask> getTasksByWarehouse(Long warehouseId);

}
