package com.furniturehome.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Double priceBeforeDiscount;
    private Long categoryId;
    private List<ProductImageDTO> images;
}
