package com.logistics.platform.distribution.settlement.reposiyory;

import com.logistics.platform.distribution.settlement.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByInvoiceNo(String invoiceNo);
    Invoice findBySettlementId(Long settlementId);
}
