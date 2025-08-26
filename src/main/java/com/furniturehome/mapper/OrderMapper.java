package com.furniturehome.mapper;

import com.furniturehome.dto.OrderDTO;
import com.furniturehome.dto.OrderRequestDTO;
import com.furniturehome.model.*;
        import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
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
//                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .userId(order.getUser() != null ? UUID.fromString(order.getUser().getId().toString()) : null)
                .orderItems(order.getOrderItems().stream()
                        .map(orderItemMapper::toOrderItemDTO)
                        .collect(Collectors.toList()))
                .orderStatusHistory(order.getOrderStatusHistory().stream()
                        .map(orderStatusHistoryMapper::toOrderStatusHistoryDTO)
                        .collect(Collectors.toList()))
//                .paymentDetails(null)     // TODO: map payment details DTO
                .build();
    }

    /**
     * @param dto order request payload
     * @param user already-fetched User entity in service layer
     */
    public Order toOrderEntity(OrderRequestDTO dto, User user) {
        if (dto == null) return null;

        Order order = Order.builder()
                .status(OrderStatus.PENDING)   // dto.getStatus() != null ? OrderStatus.valueOf(dto.getStatus()) : OrderStatus.PENDING
                .shippingAddress(dto.getShippingAddress())
                .notes(dto.getNotes())
                .user(user)
                .build();

        // Map order items using the resolved products list
        List<OrderItem> orderItems = dto.getOrderItems().stream()
                .map(itemDto -> orderItemMapper.toOrderItemEntity(itemDto, order))
                .toList();

        order.setOrderItems(orderItems);
        // recalc total from items
        order.recalculateTotal();

        return order;
    }



}
