package com.furniturehome.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequestDTO {

    @NotNull(message = "Cart must not be null")
    @Valid
    private CartDTO cart;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
}

