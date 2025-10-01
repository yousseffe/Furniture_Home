package com.furniturehome.repository;

import com.furniturehome.model.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, UUID> {
}
