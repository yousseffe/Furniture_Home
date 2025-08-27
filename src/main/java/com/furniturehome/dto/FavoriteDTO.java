package com.furniturehome.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTO {
    private Long id;
    private UUID userId;
    private Long productId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String productImage;
    private LocalDateTime createdAt;
}
