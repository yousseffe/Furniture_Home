package com.furniturehome.dto;

import com.furniturehome.model.OrderStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusHistoryDTO {
    private UUID id;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private UUID orderId;
}
