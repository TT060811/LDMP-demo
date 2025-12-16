package com.logistics.platform.distribution.settlement.service;


import com.logistics.platform.distribution.settlement.entity.SettlementOrder;
import java.util.List;

public interface SettlementOrderService {
    SettlementOrder createSettlement(SettlementOrder settlementOrder);
    List<SettlementOrder> getAllSettlements();
    SettlementOrder getSettlementById(Long id);
    SettlementOrder getSettlementByNo(String settlementNo);
    SettlementOrder updateSettlement(Long id, SettlementOrder settlementOrder);
    void deleteSettlement(Long id);
    SettlementOrder confirmSettlement(Long id);
    SettlementOrder cancelSettlement(Long id);

}
