package com.trade.repository;

import com.trade.modal.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {
    PaymentDetails findByUserId(Long userId);
}
