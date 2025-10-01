package com.furniturehome.mapper;

import com.furniturehome.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartToOrderMapper {

    public OrderRequestDTO mapCartToOrderRequest(CheckoutRequestDTO checkoutRequestDTO) {
        CartDTO cart = checkoutRequestDTO.getCart();
        return OrderRequestDTO.builder()
                .userId(cart.getUserid() != null ? cart.getUserid() : null)
                .shippingAddress(checkoutRequestDTO.getShippingAddress())
                .notes(checkoutRequestDTO.getNotes() != null ? checkoutRequestDTO.getNotes() : "")
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
                .toList();
    }
}
