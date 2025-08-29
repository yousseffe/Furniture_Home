package com.furniturehome.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusRequestDTO {
    @NotNull(message = "Order ID is required")
    private UUID orderId;

    @NotBlank(message = "Status is required")
    private String status;
}
