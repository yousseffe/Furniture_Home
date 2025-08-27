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
public class OrderResponseDTO {
    private UUID id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Double total;
    private String status;

    private String shippingAddress;
    private String notes;

    private UUID userId;
}

