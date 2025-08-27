package com.furniturehome.mapper;

import com.furniturehome.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartToOrderMapper {    //TODO: add conversion methods to regular DTOs

    public OrderRequestDTO mapCartToOrderRequest(CartDTO cart, String shippingAddress, String notes) {
        return OrderRequestDTO.builder()
                .userId(cart.getUserid() != null ? UUID.fromString(cart.getUserid().toString()) : null)
                .shippingAddress(shippingAddress)
                .notes(notes != null ? notes : "")
                .orderItems(mapCartItemsToOrderItems(cart.getItems()))
                .build();
    }

    private List<OrderItemRequestDTO> mapCartItemsToOrderItems(List<CartItemDTO> cartItems) {
        if (cartItems == null) return List.of();

        return cartItems.stream()
                .map(item -> OrderItemRequestDTO.builder()
                        .productId(item.getProductId())
                        .quantity(item.getQuantity())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
