package com.logistics.platform.distribution.delivery.repository;

import com.logistics.platform.distribution.delivery.entity.Deliveryman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliverymanRepository extends JpaRepository<Deliveryman, Long> {
    Optional<Deliveryman> findByEmployeeNo(String employeeNo);
    List<Deliveryman> findByStatus(String status);
    Optional<Deliveryman> findByPhone(String phone);
    boolean existsByEmployeeNo(String employeeNo);

}
