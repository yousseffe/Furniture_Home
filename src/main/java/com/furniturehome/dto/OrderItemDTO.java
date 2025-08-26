package com.furniturehome.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    private UUID id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double total;
}
