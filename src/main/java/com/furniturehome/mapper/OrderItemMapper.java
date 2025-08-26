package com.furniturehome.mapper;

import com.furniturehome.dto.OrderItemDTO;
import com.furniturehome.dto.OrderItemRequestDTO;
import com.furniturehome.model.Order;
import com.furniturehome.model.OrderItem;
import com.furniturehome.model.Product;
import com.furniturehome.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {

    private ProductRepository productRepository;

    @Autowired
    public OrderItemMapper(ProductRepository productRepository) {
        this.productRepository =  productRepository;
    }

    public OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        if (orderItem == null) return null;

        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct() != null ? orderItem.getProduct().getId() : null)
                .productName(orderItem.getProduct() != null ? orderItem.getProduct().getName() : null)
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .total(orderItem.getTotalPrice())
                .build();
    }

    public OrderItem toOrderItemEntity(OrderItemRequestDTO dto, Order order) {
        if (dto == null || order == null ) return null;

        long longId = dto.getProductId().hashCode(); // REMOVE HASHCODE completely wrong
        Product product = productRepository.findById(longId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.getProductId()));

        return OrderItem.builder()
                .quantity(dto.getQuantity())
                .unitPrice(product.getPrice())
                .order(order)
                .product(product)
                .build();
    }
}
