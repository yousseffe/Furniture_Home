package com.furniturehome.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDTO {
    
    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    private Long productId;
    
    @Positive(message = "Quantity must be positive")
    @Builder.Default
    private Integer quantity = 1;
    
    // Custom getter to ensure quantity is never null
    public Integer getQuantity() {
        return quantity != null ? quantity : 1;
    }
}
