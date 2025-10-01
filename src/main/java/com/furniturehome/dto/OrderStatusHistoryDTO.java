package com.furniturehome.dto;

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
    private String status;
    private LocalDateTime createdAt;
    private UUID orderId;
}
