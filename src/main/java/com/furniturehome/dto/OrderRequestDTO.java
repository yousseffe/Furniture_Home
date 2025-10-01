package com.furniturehome.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {
    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotEmpty(message = "Shipping address is required")
    @Size(max = 255, message = "Shipping address cannot exceed 255 characters")
    private String shippingAddress;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid // cascade validation into each OrderItemRequestDTO
    private List<OrderItemRequestDTO> orderItems;

    // private PaymentDetailsRequestDTO paymentDetails; // validate later
}
