package com.logistics.platform.distribution.settlement.controller;

import com.logistics.platform.distribution.settlement.entity.SettlementOrder;
import com.logistics.platform.distribution.settlement.reposiyory.SettlementOrderRepository;
import com.logistics.platform.distribution.settlement.service.SettlementOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/settlement")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementOrderService settlementOrderService;
    private final SettlementOrderRepository settlementOrderRepository;

    // 创建结算单
    @PostMapping
    public SettlementOrder createSettlement(@RequestBody SettlementOrder settlementOrder) {
        return settlementOrderService.createSettlement(settlementOrder);
    }

    // 获取所有结算单
    @GetMapping
    public List<SettlementOrder> getAllSettlements() {
        return settlementOrderService.getAllSettlements();
    }

    // 根据ID获取结算单
    @GetMapping("/{id}")
    public SettlementOrder getSettlementById(@PathVariable Long id) {
        return settlementOrderService.getSettlementById(id);
    }

    // 根据结算单号获取结算单
    @GetMapping("/no/{settlementNo}")
    public SettlementOrder getSettlementByNo(@PathVariable String settlementNo) {
        return settlementOrderService.getSettlementByNo(settlementNo);
    }

    // 根据运单ID获取结算单
    @GetMapping("/waybill/{waybillId}")
    public SettlementOrder getSettlementByWaybill(@PathVariable Long waybillId) {
        return settlementOrderRepository.findByWaybillId(waybillId);
    }

    // 更新结算单
    @PutMapping("/{id}")
    public SettlementOrder updateSettlement(@PathVariable Long id,
                                            @RequestBody SettlementOrder settlementOrder) {
        return settlementOrderService.updateSettlement(id, settlementOrder);
    }

    // 删除结算单
    @DeleteMapping("/{id}")
    public void deleteSettlement(@PathVariable Long id) {
        settlementOrderService.deleteSettlement(id);
    }

    // 确认结算
    @PostMapping("/{id}/confirm")
    public SettlementOrder confirmSettlement(@PathVariable Long id) {
        return settlementOrderService.confirmSettlement(id);
    }

    // 取消结算
    @PostMapping("/{id}/cancel")
    public SettlementOrder cancelSettlement(@PathVariable Long id) {
        return settlementOrderService.cancelSettlement(id);
    }
}