package com.logistics.platform.distribution.settlement.reposiyory;

import com.logistics.platform.distribution.settlement.entity.Reconciliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReconciliationRepository extends JpaRepository<Reconciliation, Long> {

    Optional<Reconciliation> findByReconciliationNo(String reconciliationNo);

}
