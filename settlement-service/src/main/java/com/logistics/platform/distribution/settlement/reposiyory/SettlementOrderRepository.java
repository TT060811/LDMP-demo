package com.logistics.platform.distribution.settlement.reposiyory;

import com.logistics.platform.distribution.settlement.entity.SettlementOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettlementOrderRepository extends JpaRepository<SettlementOrder, Long> {
    Optional<SettlementOrder> findBySettlementNo(String settlementNo);
    boolean existsBySettlementNo(String settlementNo);
    SettlementOrder findByWaybillId(Long waybillId);
}
