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
    private Long productId; //TODO: Change to uuid
//    private UUID productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double total;
}
