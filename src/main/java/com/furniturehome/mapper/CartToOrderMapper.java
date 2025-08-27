package com.furniturehome.mapper;

import com.furniturehome.dto.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CartToOrderMapper {

    public static OrderRequestDTO mapCartToOrderRequest(CartDTO cart, String shippingAddress, String notes) {
        return OrderRequestDTO.builder()
                .userId(cart.getUserid() != null ? UUID.fromString(cart.getUserid().toString()) : null)
                .shippingAddress(shippingAddress)
                .notes(notes)
                .orderItems(mapCartItemsToOrderItems(cart.getItems()))
                .build();
    }

    private static List<OrderItemRequestDTO> mapCartItemsToOrderItems(List<CartItemDTO> cartItems) {
        if (cartItems == null) return List.of();

        return cartItems.stream()
                .map(item -> OrderItemRequestDTO.builder()
                        .productId(UUID.fromString(item.getProductId().toString()))
                        .quantity(item.getQuantity())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
