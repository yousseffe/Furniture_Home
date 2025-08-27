package com.furniturehome.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDTO {
    private Long productId; //TODO: Change to uuid
//    private UUID productId;
    private Integer quantity;
}
