package com.furniturehome.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequestDTO {
    CartDTO cart;
    private String shippingAddress;
    private String notes;
}
