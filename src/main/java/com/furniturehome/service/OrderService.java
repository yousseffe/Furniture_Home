package com.furniturehome.service;

import com.furniturehome.dto.*;
import com.furniturehome.exception.BadRequestException;
import com.furniturehome.exception.ResourceNotFoundException;
import com.furniturehome.model.*;
import com.furniturehome.repository.*;
import com.furniturehome.mapper.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final CartToOrderMapper cartToOrderMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderStatusHistoryMapper orderStatusHistoryMapper;

    @Transactional
    public OrderDTO createOrder(CheckoutRequestDTO checkoutRequestDTO){
        OrderRequestDTO orderDTO = cartToOrderMapper.mapCartToOrderRequest(checkoutRequestDTO);

        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + orderDTO.getUserId()));

        Order order = orderMapper.toOrderEntity(orderDTO, user);

        Map<Long, Product> products = fetchProductsMap(orderDTO.getOrderItems());     // TODO: change long to uuid

        List<OrderItem> orderItems = orderDTO.getOrderItems().stream()
                .map(orderItemDto -> {
                    Product product = products.get(orderItemDto.getProductId());
                    return orderItemMapper.toOrderItemEntity(orderItemDto, order, product);
                })
                .toList();

        order.setOrderItems(orderItems);
        order.recalculateTotal();

        OrderStatusHistory orderStatusHistory = OrderStatusHistory.builder()
                .status(order.getStatus())
                .order(order)
                .build();

        order.addOrderStatusHistory(orderStatusHistory);

//        cartRepository.deleteById(cartDTO.getId()); // or mark inactive

        // with cascade, items persist automatically
        return orderMapper.toOrderDTO(orderRepository.save(order));

    }

    @Transactional
    public OrderDTO createOrder(OrderRequestDTO orderDTO){
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + orderDTO.getUserId()));

        Order order = orderMapper.toOrderEntity(orderDTO, user);

        Map<Long, Product> products = fetchProductsMap(orderDTO.getOrderItems());     // TODO: change long to uuid

        List<OrderItem> orderItems = orderDTO.getOrderItems().stream()
                .map(orderItemDto -> {
                    Product product = products.get(orderItemDto.getProductId());
                    return orderItemMapper.toOrderItemEntity(orderItemDto, order, product);
                })
                .toList();

        order.setOrderItems(orderItems);
        order.recalculateTotal();

        OrderStatusHistory orderStatusHistory = OrderStatusHistory.builder()
                .status(order.getStatus())
                .order(order)
                .build();

        order.addOrderStatusHistory(orderStatusHistory);

        return orderMapper.toOrderDTO(orderRepository.save(order)); // with cascade, items persist automatically
    }

    private Map<Long, Product> fetchProductsMap(List<OrderItemRequestDTO> items) {
        List<Long> productIds = items.stream()
                .map(OrderItemRequestDTO::getProductId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (productIds.isEmpty()) return Collections.emptyMap();

        Map<Long, Product> productsById = productRepository.findAllById(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        Set<Long> missing = productIds.stream()
                .filter(id -> !productsById.containsKey(id))
                .collect(Collectors.toSet());
        if (!missing.isEmpty()) {
            throw new ResourceNotFoundException("Missing products: " + missing);
        }
        return productsById;
    }

    @Transactional
    public OrderDTO updateOrderStatus(OrderStatusRequestDTO orderStatusRequestDTO) {
        Order order = orderRepository.findById(orderStatusRequestDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderStatusRequestDTO.getOrderId()));

        final OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(orderStatusRequestDTO.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid order status: " + orderStatusRequestDTO.getStatus());
        }

        validateStatusTransition(order.getStatus(), newStatus);

        order.setStatus(newStatus);

        OrderStatusHistory orderStatusHistory = OrderStatusHistory.builder()
                .status(newStatus)
                .order(order)
                .build();

        order.addOrderStatusHistory(orderStatusHistory);

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderDTO(savedOrder);
    }

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            OrderStatus.PENDING, Set.of(OrderStatus.PROCESSING, OrderStatus.CANCELLED),
            OrderStatus.PROCESSING, Set.of(OrderStatus.SHIPPED, OrderStatus.CANCELLED),
            OrderStatus.SHIPPED, Set.of(OrderStatus.DELIVERED, OrderStatus.RETURNED),
            OrderStatus.DELIVERED, Set.of(OrderStatus.RETURNED)
    );

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (!ALLOWED_TRANSITIONS.getOrDefault(currentStatus, Set.of()).contains(newStatus)) {
            throw new BadRequestException("Invalid transition from " + currentStatus + " to " + newStatus);
        }
    }

    @Transactional
    public OrderDTO cancelOrder(OrderDTO orderDTO){
        OrderStatusRequestDTO orderStatusRequestDTO = orderStatusHistoryMapper
                .toOrderStatusRequestDTO(orderDTO.getId(), "Cancelled");
        return updateOrderStatus(orderStatusRequestDTO);
    }

    @Transactional
    public OrderDTO addOrderItem(OrderItemRequestDTO orderItemRequestDTO, UUID orderId, Long productId){    //TODO: change productId type
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if ((order.getStatus() != OrderStatus.PENDING) && (order.getStatus() != OrderStatus.PROCESSING)) {
            throw new BadRequestException("Cannot add items to a " + order.getStatus() + " order");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Optional<OrderItem> existingItem = order.getOrderItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isEmpty()) {
            OrderItem orderItem = orderItemMapper.toOrderItemEntity(orderItemRequestDTO, order, product);
            order.addOrderItem(orderItem);
            order.recalculateTotal();
        }else{
            throw new BadRequestException("Product with id " + productId + " is already in the order. Try updating its quantity instead.");
        }

        return orderMapper.toOrderDTO(orderRepository.save(order));
    }

    @Transactional
    public OrderDTO deleteOrderItem(OrderItemRequestDTO orderItemRequestDTO, UUID orderId, Long productId){    //TODO: change productId type
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

            if ((order.getStatus() != OrderStatus.PENDING) && (order.getStatus() != OrderStatus.PROCESSING)) {
                throw new BadRequestException("Cannot delete items from a " + order.getStatus() + " order");
            }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Optional<OrderItem> existingItem = order.getOrderItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            order.removeOrderItem(existingItem.get());
            order.recalculateTotal();
        }else{
            throw new BadRequestException("Product with id " + productId + " is not in the order.");
        }

        return orderMapper.toOrderDTO(orderRepository.save(order));
    }

    @Transactional
    public OrderDTO updateOrderItem(UUID orderId, Long productId, Integer newQuantity){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if ((order.getStatus() != OrderStatus.PENDING) && (order.getStatus() != OrderStatus.PROCESSING)) {
                throw new BadRequestException("Cannot update items in a " + order.getStatus() + " order");
        }

        if (newQuantity == null || newQuantity < 0) {
            throw new BadRequestException("Quantity must be non-negative");
        }

        OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id " + productId + " not found in order " + orderId));

        if (newQuantity == 0) {
            order.removeOrderItem(orderItem);
        } else {
            orderItem.setQuantity(newQuantity);
        }

        order.recalculateTotal();
        return orderMapper.toOrderDTO(orderRepository.save(order));
    }

    public OrderDTO viewOrderDetails(UUID orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return orderMapper.toOrderDTO(order);
    }

    public List<OrderResponseDTO> viewUserOrders(UUID userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        List<Order> orders = orderRepository.findAllByUser(user);

        return orders.stream()
                .map(orderMapper::toOrderResponseDTO)
                .filter(order -> !"Cancelled".equalsIgnoreCase(order.getStatus()))
                .toList();

    }

    public List<OrderResponseDTO> viewUserOrdersById(UUID userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);

        return orders.stream()
                .map(orderMapper::toOrderResponseDTO)
                .toList();
    }

    public List<OrderStatusHistoryDTO> viewOrderStatusHistory(UUID orderId){
        List<OrderStatusHistory> history = orderStatusHistoryRepository.findAllByOrderId(orderId);

        return history.stream()
                .map(orderStatusHistoryMapper::toOrderStatusHistoryDTO)
                .toList();
    }

}

