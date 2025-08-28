package com.furniturehome.controller;

import com.furniturehome.dto.*;
import com.furniturehome.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // ----------------- CREATE -----------------

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        return ResponseEntity.ok(orderService.createOrder(orderRequestDTO));
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> checkoutOrder(@RequestBody CheckoutRequestDTO checkoutRequestDTO) {
        return ResponseEntity.ok(orderService.createOrder(checkoutRequestDTO));
    }

    // ----------------- STATUS -----------------

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable UUID orderId,
                                                      @RequestBody OrderStatusRequestDTO request) {
        // Ensure the orderId in URL and body match (optional validation)
        if (!orderId.equals(request.getOrderId())) {
            throw new IllegalArgumentException("Order ID mismatch between path and request body");
        }
        return ResponseEntity.ok(orderService.updateOrderStatus(request));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable UUID orderId) {
        OrderDTO dto = orderService.viewOrderDetails(orderId);
        return ResponseEntity.ok(orderService.cancelOrder(dto));
    }

    // ----------------- ORDER ITEMS -----------------

    @PostMapping("/{orderId}/items/{productId}")
    public ResponseEntity<OrderDTO> addOrderItem(@RequestBody OrderItemRequestDTO orderItemRequestDTO,
                                                 @PathVariable UUID orderId,
                                                 @PathVariable Long productId) {
        return ResponseEntity.ok(orderService.addOrderItem(orderItemRequestDTO, orderId, productId));
    }

    @DeleteMapping("/{orderId}/items/{productId}")
    public ResponseEntity<OrderDTO> deleteOrderItem(@RequestBody OrderItemRequestDTO orderItemRequestDTO,
                                                    @PathVariable UUID orderId,
                                                    @PathVariable Long productId) {
        return ResponseEntity.ok(orderService.deleteOrderItem(orderItemRequestDTO, orderId, productId));
    }

    @PutMapping("/{orderId}/items/{productId}")
    public ResponseEntity<OrderDTO> updateOrderItem(@PathVariable UUID orderId,
                                                    @PathVariable Long productId,
                                                    @RequestParam Integer quantity) {
        return ResponseEntity.ok(orderService.updateOrderItem(orderId, productId, quantity));
    }

    // ----------------- READ -----------------

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> viewOrderDetails(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.viewOrderDetails(orderId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> viewUserOrders(@PathVariable UUID userId,
                                                                 @RequestParam(defaultValue = "false") boolean byIdOnly) {
        if (byIdOnly) {
            return ResponseEntity.ok(orderService.viewUserOrdersById(userId));
        }
        return ResponseEntity.ok(orderService.viewUserOrders(userId));
    }
}

