package com.furniturehome.mapper;

import com.furniturehome.dto.OrderDTO;
import com.furniturehome.dto.OrderRequestDTO;
import com.furniturehome.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.furniturehome.dto.OrderDTO;
import com.furniturehome.dto.OrderRequestDTO;
import com.furniturehome.model.Order;
import com.furniturehome.model.OrderItem;
import com.furniturehome.model.OrderStatus;
import com.furniturehome.model.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {

        private final OrderItemMapper orderItemMapper;
        private final OrderStatusHistoryMapper orderStatusHistoryMapper;

        public OrderDTO toOrderDTO(Order order) {
                if (order == null) {
                        return null;
                }

                return OrderDTO.builder()
                        .id(order.getId())
                        .createdAt(order.getCreatedAt())
                        .updatedAt(order.getUpdatedAt())
                        .total(order.getTotal())
                        .status(order.getStatus().name())
                        .shippingAddress(order.getShippingAddress())
                        .notes(order.getNotes())
                        .userId(order.getUser() != null ? UUID.fromString(order.getUser().getId().toString()) : null)
                        .orderItems(order.getOrderItems().stream()
                        .map(orderItemMapper::toOrderItemDTO)
                        .collect(Collectors.toList()))
                        .orderStatusHistory(order.getOrderStatusHistory().stream()
                        .map(orderStatusHistoryMapper::toOrderStatusHistoryDTO)
                        .collect(Collectors.toList()))
                .build();
        }

        public Order toOrderEntity(OrderRequestDTO dto, User user) {
                if (dto == null) {
                        return null;
                }

                Order order = Order.builder()
                        .status(OrderStatus.PENDING) 
                        .shippingAddress(dto.getShippingAddress())
                        .notes(dto.getNotes())
                        .user(user)
                        .build();

                List<OrderItem> orderItems = dto.getOrderItems().stream()
                        .map(itemDto -> orderItemMapper.toOrderItemEntity(itemDto, order))
                        .toList();

                order.setOrderItems(orderItems);
                order.recalculateTotal();

                return order;
        }

}
