package com.furniturehome.mapper;

import com.furniturehome.dto.OrderDTO;
import com.furniturehome.dto.OrderRequestDTO;
import com.furniturehome.dto.OrderResponseDTO;
import com.furniturehome.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;
    private final OrderStatusHistoryMapper orderStatusHistoryMapper;

    public OrderDTO toOrderDTO(Order order) {
        if (order == null) return null;

        return OrderDTO.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .total(order.getTotal())
                .status(order.getStatus().name())
                .shippingAddress(order.getShippingAddress())
                .notes(order.getNotes())
                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .orderItems(order.getOrderItems().stream()
                        .map(orderItemMapper::toOrderItemDTO)
                        .collect(Collectors.toList()))
                .orderStatusHistory(order.getOrderStatusHistory().stream()
                        .map(orderStatusHistoryMapper::toOrderStatusHistoryDTO)
                        .collect(Collectors.toList()))
//                .paymentDetails(null)     // TODO: map payment details DTO
                .build();
    }


    public Order toOrderEntity(OrderRequestDTO dto, User user) {
        if (dto == null) return null;

        return Order.builder()
                .status(OrderStatus.PENDING)
                .shippingAddress(dto.getShippingAddress())
                .notes(dto.getNotes())
                .user(user)
                .build();
    }

    public OrderResponseDTO toOrderResponseDTO(Order order){
        if (order == null) return null;

        return OrderResponseDTO.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .total(order.getTotal())
                .total(order.getTotal())
                .status(order.getStatus().name())
                .shippingAddress(order.getShippingAddress())
                .notes(order.getNotes())
                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .build();
    }

}
