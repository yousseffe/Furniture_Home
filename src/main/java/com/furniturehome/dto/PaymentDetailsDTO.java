package com.furniturehome.dto;

import com.furniturehome.model.PaymentMethod;
import com.furniturehome.model.PaymentStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetailsDTO {
    private UUID id;
    private Double amount;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private UUID orderid;
}
