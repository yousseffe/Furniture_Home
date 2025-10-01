package com.furniturehome.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long id;
    private Long cartId;
    private Long productId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String productImage;
    private Integer quantity;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
