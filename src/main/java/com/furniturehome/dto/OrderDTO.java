package com.furniturehome.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private UUID id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Double total;
    private String status;

    private String shippingAddress;
    private String notes;

//    private UUID userId;
    private Integer userId;     //TODO: Change to uuid

    private List<OrderItemDTO> orderItems;
    private List<OrderStatusHistoryDTO> orderStatusHistory;
//    private PaymentDetailsDTO paymentDetails;
}

