package com.logistics.platform.distribution.delivery.service.impl;

import com.logistics.platform.distribution.delivery.entity.Deliveryman;
import com.logistics.platform.distribution.delivery.entity.DeliveryTask;
import com.logistics.platform.distribution.delivery.repository.DeliverymanRepository;
import com.logistics.platform.distribution.delivery.repository.DeliveryTaskRepository;
import com.logistics.platform.distribution.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliverymanRepository deliverymanRepository;
    private final DeliveryTaskRepository deliveryTaskRepository;

    // 配送员管理方法
    @Override
    @Transactional
    public Deliveryman createDeliveryman(Deliveryman deliveryman) {
        // 生成员工工号
        if (deliveryman.getEmployeeNo() == null) {
            String employeeNo = "DM" + System.currentTimeMillis();
            deliveryman.setEmployeeNo(employeeNo);
        }

        // 检查工号是否重复
        if (deliverymanRepository.existsByEmployeeNo(deliveryman.getEmployeeNo())) {
            throw new RuntimeException("配送员工号已存在: " + deliveryman.getEmployeeNo());
        }

        // 设置默认值
        if (deliveryman.getStatus() == null) {
            deliveryman.setStatus("ACTIVE");
        }
        if (deliveryman.getTotalOrders() == null) {
            deliveryman.setTotalOrders(0);
        }
        if (deliveryman.getRating() == null) {
            deliveryman.setRating(new BigDecimal("5.00"));
        }

        // 设置创建时间
        deliveryman.setCreateTime(LocalDateTime.now());

        return deliverymanRepository.save(deliveryman);
    }

    @Override
    public List<Deliveryman> getAllDeliverymen() {
        return deliverymanRepository.findAll();
    }

    @Override
    public Deliveryman getDeliverymanById(Long id) {
        return deliverymanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("配送员不存在: " + id));
    }

    @Override
    @Transactional
    public Deliveryman updateDeliveryman(Long id, Deliveryman deliveryman) {
        Deliveryman existing = getDeliverymanById(id);

        // 更新基本信息
        existing.setName(deliveryman.getName());
        existing.setPhone(deliveryman.getPhone());
        existing.setIdCard(deliveryman.getIdCard());
        existing.setStatus(deliveryman.getStatus());

        return deliverymanRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteDeliveryman(Long id) {
        Deliveryman deliveryman = getDeliverymanById(id);

        // 检查是否有正在进行的任务
        if (deliveryman.getCurrentOrderId() != null) {
            throw new RuntimeException("配送员有进行中的任务，无法删除");
        }

        deliverymanRepository.delete(deliveryman);
    }

    @Override
    @Transactional
    public Deliveryman updateDeliverymanLocation(Long id, BigDecimal latitude, BigDecimal longitude) {
        Deliveryman deliveryman = getDeliverymanById(id);
        deliveryman.setCurrentLatitude(latitude);
        deliveryman.setCurrentLongitude(longitude);

        log.info("更新配送员 {} 位置: ({}, {})", id, latitude, longitude);

        return deliverymanRepository.save(deliveryman);
    }

    @Override
    public List<Deliveryman> getAvailableDeliverymen() {
        return deliverymanRepository.findByStatus("ACTIVE").stream()
                .filter(deliveryman -> deliveryman.getCurrentOrderId() == null)
                .toList();
    }

    // 配送任务管理方法
    @Override
    @Transactional
    public DeliveryTask createDeliveryTask(DeliveryTask deliveryTask) {
        // 生成任务编号
        if (deliveryTask.getTaskNo() == null) {
            String taskNo = "DT" + System.currentTimeMillis();
            deliveryTask.setTaskNo(taskNo);
        }

        // 设置默认状态
        if (deliveryTask.getStatus() == null) {
            deliveryTask.setStatus("ASSIGNED");
        }

        // 设置创建时间和分配时间
        deliveryTask.setCreateTime(LocalDateTime.now());
        if ("ASSIGNED".equals(deliveryTask.getStatus()) && deliveryTask.getAssignedTime() == null) {
            deliveryTask.setAssignedTime(LocalDateTime.now());
        }

        return deliveryTaskRepository.save(deliveryTask);
    }

    @Override
    public List<DeliveryTask> getAllDeliveryTasks() {
        return deliveryTaskRepository.findAll();
    }

    @Override
    public DeliveryTask getDeliveryTaskById(Long id) {
        return deliveryTaskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("配送任务不存在: " + id));
    }

    @Override
    public DeliveryTask getDeliveryTaskByNo(String taskNo) {
        return deliveryTaskRepository.findByTaskNo(taskNo)
                .orElseThrow(() -> new RuntimeException("配送任务不存在: " + taskNo));
    }

    @Override
    @Transactional
    public DeliveryTask assignDeliveryTask(Long taskId, Long deliverymanId) {
        DeliveryTask task = getDeliveryTaskById(taskId);
        Deliveryman deliveryman = getDeliverymanById(deliverymanId);

        // 检查配送员是否可用
        if (!"ACTIVE".equals(deliveryman.getStatus())) {
            throw new RuntimeException("配送员状态不可用");
        }
        if (deliveryman.getCurrentOrderId() != null) {
            throw new RuntimeException("配送员已有进行中的任务");
        }

        // 更新任务信息
        task.setDeliverymanId(deliverymanId);
        task.setStatus("ASSIGNED");
        task.setAssignedTime(LocalDateTime.now());

        // 更新配送员信息
        deliveryman.setCurrentOrderId(taskId);
        deliverymanRepository.save(deliveryman);

        return deliveryTaskRepository.save(task);
    }

    @Override
    @Transactional
    public DeliveryTask updateTaskStatus(Long taskId, String status) {
        DeliveryTask task = getDeliveryTaskById(taskId);
        String oldStatus = task.getStatus();

        // 状态校验
        switch (status) {
            case "PICKED_UP":
                if (!"ASSIGNED".equals(oldStatus)) {
                    throw new RuntimeException("只能从'已分配'状态更新为'已取货'");
                }
                task.setPickupTime(LocalDateTime.now());
                break;

            case "DELIVERING":
                if (!"PICKED_UP".equals(oldStatus)) {
                    throw new RuntimeException("只能从'已取货'状态更新为'配送中'");
                }
                task.setStartDeliveryTime(LocalDateTime.now());
                break;

            case "DELIVERED":
                if (!"DELIVERING".equals(oldStatus)) {
                    throw new RuntimeException("只能从'配送中'状态更新为'已送达'");
                }
                task.setActualDeliveryTime(LocalDateTime.now());

                // 更新配送员信息
                Deliveryman deliveryman = deliverymanRepository.findById(task.getDeliverymanId())
                        .orElse(null);
                if (deliveryman != null) {
                    deliveryman.setTotalOrders(deliveryman.getTotalOrders() + 1);
                    deliveryman.setCurrentOrderId(null);
                    deliverymanRepository.save(deliveryman);
                }
                break;

            case "CANCELLED":
                if ("DELIVERED".equals(oldStatus)) {
                    throw new RuntimeException("已送达的任务不能取消");
                }

                // 释放配送员
                Deliveryman deliveryman2 = deliverymanRepository.findById(task.getDeliverymanId())
                        .orElse(null);
                if (deliveryman2 != null) {
                    deliveryman2.setCurrentOrderId(null);
                    deliverymanRepository.save(deliveryman2);
                }
                break;
        }

        task.setStatus(status);
        log.info("配送任务 {} 状态更新: {} -> {}", taskId, oldStatus, status);

        return deliveryTaskRepository.save(task);
    }

    @Override
    @Transactional
    public DeliveryTask pickUpTask(Long taskId) {
        return updateTaskStatus(taskId, "PICKED_UP");
    }

    @Override
    @Transactional
    public DeliveryTask startDelivery(Long taskId) {
        return updateTaskStatus(taskId, "DELIVERING");
    }

    @Override
    @Transactional
    public DeliveryTask completeDelivery(Long taskId) {
        return updateTaskStatus(taskId, "DELIVERED");
    }

    @Override
    @Transactional
    public DeliveryTask cancelDeliveryTask(Long taskId) {
        return updateTaskStatus(taskId, "CANCELLED");
    }

    // 查询方法
    @Override
    public List<DeliveryTask> getTasksByDeliveryman(Long deliverymanId) {
        return deliveryTaskRepository.findByDeliverymanId(deliverymanId);
    }

    @Override
    public List<DeliveryTask> getTasksByStatus(String status) {
        return deliveryTaskRepository.findByStatus(status);
    }

    @Override
    public List<DeliveryTask> getTasksByWaybill(Long waybillId) {
        return deliveryTaskRepository.findByWaybillId(waybillId);
    }

    @Override
    public List<DeliveryTask> getTasksByWarehouse(Long warehouseId) {
        return deliveryTaskRepository.findByWarehouseId(warehouseId);
    }
}