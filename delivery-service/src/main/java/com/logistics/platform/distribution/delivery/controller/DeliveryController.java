package com.logistics.platform.distribution.delivery.controller;

import com.logistics.platform.distribution.delivery.entity.Deliveryman;
import com.logistics.platform.distribution.delivery.entity.DeliveryTask;
import com.logistics.platform.distribution.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    // ========== 配送员管理 ==========

    @PostMapping("/deliverymen")
    public ResponseEntity<Deliveryman> createDeliveryman(@RequestBody Deliveryman deliveryman) {
        Deliveryman created = deliveryService.createDeliveryman(deliveryman);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/deliverymen")
    public ResponseEntity<List<Deliveryman>> getAllDeliverymen() {
        List<Deliveryman> deliverymen = deliveryService.getAllDeliverymen();
        return ResponseEntity.ok(deliverymen);
    }

    @GetMapping("/deliverymen/{id}")
    public ResponseEntity<Deliveryman> getDeliverymanById(@PathVariable Long id) {
        Deliveryman deliveryman = deliveryService.getDeliverymanById(id);
        return ResponseEntity.ok(deliveryman);
    }

    @PutMapping("/deliverymen/{id}")
    public ResponseEntity<Deliveryman> updateDeliveryman(@PathVariable Long id,
                                                         @RequestBody Deliveryman deliveryman) {
        Deliveryman updated = deliveryService.updateDeliveryman(id, deliveryman);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/deliverymen/{id}")
    public ResponseEntity<Void> deleteDeliveryman(@PathVariable Long id) {
        deliveryService.deleteDeliveryman(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/deliverymen/{id}/location")
    public ResponseEntity<Deliveryman> updateDeliverymanLocation(@PathVariable Long id,
                                                                 @RequestParam BigDecimal latitude,
                                                                 @RequestParam BigDecimal longitude) {
        Deliveryman updated = deliveryService.updateDeliverymanLocation(id, latitude, longitude);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/deliverymen/available")
    public ResponseEntity<List<Deliveryman>> getAvailableDeliverymen() {
        List<Deliveryman> available = deliveryService.getAvailableDeliverymen();
        return ResponseEntity.ok(available);
    }

    // ========== 配送任务管理 ==========

    @PostMapping("/tasks")
    public ResponseEntity<DeliveryTask> createDeliveryTask(@RequestBody DeliveryTask deliveryTask) {
        DeliveryTask created = deliveryService.createDeliveryTask(deliveryTask);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<DeliveryTask>> getAllDeliveryTasks() {
        List<DeliveryTask> tasks = deliveryService.getAllDeliveryTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<DeliveryTask> getDeliveryTaskById(@PathVariable Long id) {
        DeliveryTask task = deliveryService.getDeliveryTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/tasks/no/{taskNo}")
    public ResponseEntity<DeliveryTask> getDeliveryTaskByNo(@PathVariable String taskNo) {
        DeliveryTask task = deliveryService.getDeliveryTaskByNo(taskNo);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/tasks/{taskId}/assign/{deliverymanId}")
    public ResponseEntity<DeliveryTask> assignDeliveryTask(@PathVariable Long taskId,
                                                           @PathVariable Long deliverymanId) {
        DeliveryTask assigned = deliveryService.assignDeliveryTask(taskId, deliverymanId);
        return ResponseEntity.ok(assigned);
    }

    @PostMapping("/tasks/{taskId}/pickup")
    public ResponseEntity<DeliveryTask> pickUpTask(@PathVariable Long taskId) {
        DeliveryTask updated = deliveryService.pickUpTask(taskId);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/tasks/{taskId}/start")
    public ResponseEntity<DeliveryTask> startDelivery(@PathVariable Long taskId) {
        DeliveryTask updated = deliveryService.startDelivery(taskId);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/tasks/{taskId}/complete")
    public ResponseEntity<DeliveryTask> completeDelivery(@PathVariable Long taskId) {
        DeliveryTask updated = deliveryService.completeDelivery(taskId);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/tasks/{taskId}/cancel")
    public ResponseEntity<DeliveryTask> cancelDeliveryTask(@PathVariable Long taskId) {
        DeliveryTask updated = deliveryService.cancelDeliveryTask(taskId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/tasks/deliveryman/{deliverymanId}")
    public ResponseEntity<List<DeliveryTask>> getTasksByDeliveryman(@PathVariable Long deliverymanId) {
        List<DeliveryTask> tasks = deliveryService.getTasksByDeliveryman(deliverymanId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasks/status/{status}")
    public ResponseEntity<List<DeliveryTask>> getTasksByStatus(@PathVariable String status) {
        List<DeliveryTask> tasks = deliveryService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasks/waybill/{waybillId}")
    public ResponseEntity<List<DeliveryTask>> getTasksByWaybill(@PathVariable Long waybillId) {
        List<DeliveryTask> tasks = deliveryService.getTasksByWaybill(waybillId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/tasks/warehouse/{warehouseId}")
    public ResponseEntity<List<DeliveryTask>> getTasksByWarehouse(@PathVariable Long warehouseId) {
        List<DeliveryTask> tasks = deliveryService.getTasksByWarehouse(warehouseId);
        return ResponseEntity.ok(tasks);
    }
}