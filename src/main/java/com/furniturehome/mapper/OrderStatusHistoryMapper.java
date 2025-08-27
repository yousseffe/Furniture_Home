package com.furniturehome.mapper;

import com.furniturehome.dto.OrderDTO;
import com.furniturehome.dto.OrderStatusHistoryDTO;
import com.furniturehome.dto.OrderStatusRequestDTO;
import com.furniturehome.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderStatusHistoryMapper {

    public OrderStatusHistoryDTO toOrderStatusHistoryDTO (OrderStatusHistory orderStatusHistory) {
        if (orderStatusHistory == null) return null;

        return OrderStatusHistoryDTO.builder()
                .id(orderStatusHistory.getId())
                .status(orderStatusHistory.getStatus().name())
                .createdAt(orderStatusHistory.getCreatedAt())
                .orderId(orderStatusHistory.getOrder().getId())
                .build();
    }

    public OrderStatusHistory toOrderStatusHistoryEntity (OrderStatusHistoryDTO dto, Order order) {
        if (dto == null) return null;


        return OrderStatusHistory.builder()
                .id(dto.getId() != null ? dto.getId() : null)
                .status(OrderStatus.valueOf(dto.getStatus()))
                .createdAt(dto.getCreatedAt())
                .order(order)
                .build();
    }

    public OrderStatusRequestDTO toOrderStatusRequestDTO(UUID orderId, String status) {
        if (orderId == null || status ==  null) return null;

        return OrderStatusRequestDTO.builder()
                .orderId(orderId)
                .status(status)
                .build();
    }
}
