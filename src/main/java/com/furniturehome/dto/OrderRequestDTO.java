package com.furniturehome.dto;

import lombok.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {
    private UUID userId; // reference to the user placing the order
    private String shippingAddress;
    private String notes;

    private List<OrderItemRequestDTO> orderItems;

//    private PaymentDetailsRequestDTO paymentDetails;
}
