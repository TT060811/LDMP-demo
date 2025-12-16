// ReconciliationController.java
package com.logistics.platform.distribution.settlement.controller;

import com.logistics.platform.distribution.settlement.entity.Reconciliation;

import com.logistics.platform.distribution.settlement.reposiyory.ReconciliationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reconciliation")
@RequiredArgsConstructor
public class ReconciliationController {

    private final ReconciliationRepository reconciliationRepository;

    // 创建对账单
    @PostMapping
    public Reconciliation createReconciliation(@RequestBody Reconciliation reconciliation) {
        // 生成对账单号
        if (reconciliation.getReconciliationNo() == null) {
            String reconciliationNo = "RC" + System.currentTimeMillis();
            reconciliation.setReconciliationNo(reconciliationNo);
        }
        return reconciliationRepository.save(reconciliation);
    }

    // 获取所有对账单
    @GetMapping
    public List<Reconciliation> getAllReconciliations() {
        return reconciliationRepository.findAll();
    }

    // 根据ID获取对账单
    @GetMapping("/{id}")
    public Reconciliation getReconciliationById(@PathVariable Long id) {
        return reconciliationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("对账单不存在: " + id));
    }

    // 更新对账单
    @PutMapping("/{id}")
    public Reconciliation updateReconciliation(@PathVariable Long id,
                                               @RequestBody Reconciliation reconciliation) {
        Reconciliation existing = getReconciliationById(id);
        existing.setStartDate(reconciliation.getStartDate());
        existing.setEndDate(reconciliation.getEndDate());
        existing.setTotalOrders(reconciliation.getTotalOrders());
        existing.setTotalAmount(reconciliation.getTotalAmount());
        existing.setRemark(reconciliation.getRemark());
        return reconciliationRepository.save(existing);
    }

    // 删除对账单
    @DeleteMapping("/{id}")
    public void deleteReconciliation(@PathVariable Long id) {
        Reconciliation reconciliation = getReconciliationById(id);
        reconciliationRepository.delete(reconciliation);
    }

    // 审核对账单
    @PostMapping("/{id}/check")
    public Reconciliation checkReconciliation(@PathVariable Long id,
                                              @RequestParam Long checkerId) {
        Reconciliation reconciliation = getReconciliationById(id);
        reconciliation.setReconciliationStatus("CHECKED");
        reconciliation.setCheckerId(checkerId);
        reconciliation.setCheckTime(java.time.LocalDateTime.now());
        return reconciliationRepository.save(reconciliation);
    }
}