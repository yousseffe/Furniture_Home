package com.furniturehome.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusRequestDTO {
    private String status;
    private UUID orderId;
}
