// SettlementOrderServiceImpl.java
package com.logistics.platform.distribution.settlement.service.Impl;

import com.logistics.platform.distribution.settlement.entity.SettlementOrder;

import com.logistics.platform.distribution.settlement.reposiyory.SettlementOrderRepository;
import com.logistics.platform.distribution.settlement.service.SettlementOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementOrderServiceImpl implements SettlementOrderService {

    private final SettlementOrderRepository settlementOrderRepository;

    @Override
    public SettlementOrder createSettlement(SettlementOrder settlementOrder) {
        // 生成结算单号
        if (settlementOrder.getSettlementNo() == null) {
            String settlementNo = "ST" + System.currentTimeMillis();
            settlementOrder.setSettlementNo(settlementNo);
        }

        // 计算总费用
        if (settlementOrder.getBaseFee() != null) {
            if (settlementOrder.getExtraFee() == null) {
                settlementOrder.setExtraFee(java.math.BigDecimal.ZERO);
            }
            settlementOrder.setTotalFee(settlementOrder.getBaseFee().add(settlementOrder.getExtraFee()));
        }

        return settlementOrderRepository.save(settlementOrder);
    }

    @Override
    public List<SettlementOrder> getAllSettlements() {
        return settlementOrderRepository.findAll();
    }

    @Override
    public SettlementOrder getSettlementById(Long id) {
        return settlementOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("结算单不存在: " + id));
    }

    @Override
    public SettlementOrder getSettlementByNo(String settlementNo) {
        return settlementOrderRepository.findBySettlementNo(settlementNo)
                .orElseThrow(() -> new RuntimeException("结算单不存在: " + settlementNo));
    }

    @Override
    public SettlementOrder updateSettlement(Long id, SettlementOrder settlementOrder) {
        SettlementOrder existing = getSettlementById(id);

        // 更新基本信息
        existing.setBaseFee(settlementOrder.getBaseFee());
        existing.setExtraFee(settlementOrder.getExtraFee());
        existing.setRemark(settlementOrder.getRemark());

        // 重新计算总费用
        if (existing.getBaseFee() != null) {
            if (existing.getExtraFee() == null) {
                existing.setExtraFee(java.math.BigDecimal.ZERO);
            }
            existing.setTotalFee(existing.getBaseFee().add(existing.getExtraFee()));
        }

        return settlementOrderRepository.save(existing);
    }

    @Override
    public void deleteSettlement(Long id) {
        SettlementOrder settlementOrder = getSettlementById(id);
        settlementOrderRepository.delete(settlementOrder);
    }

    @Override
    @Transactional
    public SettlementOrder confirmSettlement(Long id) {
        SettlementOrder settlementOrder = getSettlementById(id);

        if ("SETTLED".equals(settlementOrder.getSettlementStatus())) {
            throw new RuntimeException("结算单已结算");
        }

        if ("CANCELLED".equals(settlementOrder.getSettlementStatus())) {
            throw new RuntimeException("结算单已取消，无法结算");
        }

        settlementOrder.setSettlementStatus("SETTLED");
        settlementOrder.setSettlementTime(LocalDateTime.now());
        settlementOrder.setPaymentStatus("PAID");
        settlementOrder.setPaymentTime(LocalDateTime.now());

        return settlementOrderRepository.save(settlementOrder);
    }

    @Override
    @Transactional
    public SettlementOrder cancelSettlement(Long id) {
        SettlementOrder settlementOrder = getSettlementById(id);

        if ("CANCELLED".equals(settlementOrder.getSettlementStatus())) {
            throw new RuntimeException("结算单已取消");
        }

        if ("SETTLED".equals(settlementOrder.getSettlementStatus())) {
            throw new RuntimeException("结算单已结算，无法取消");
        }

        settlementOrder.setSettlementStatus("CANCELLED");
        settlementOrder.setSettlementTime(LocalDateTime.now());

        return settlementOrderRepository.save(settlementOrder);
    }
}