package com.furniturehome.controller;

import com.furniturehome.dto.*;
import com.furniturehome.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // ----------------- CREATE -----------------

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        return ResponseEntity.ok(orderService.createOrder(orderRequestDTO));
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> checkoutOrder(@Valid @RequestBody CheckoutRequestDTO checkoutRequestDTO) {
        return ResponseEntity.ok(orderService.createOrder(checkoutRequestDTO));
    }

    // ----------------- STATUS -----------------

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@Valid @PathVariable UUID orderId,
                                                      @RequestBody OrderStatusRequestDTO request) {
        // Ensure the orderId in URL and body match (optional validation)
        if (!orderId.equals(request.getOrderId())) {
            throw new IllegalArgumentException("Order ID mismatch between path and request body");
        }
        return ResponseEntity.ok(orderService.updateOrderStatus(request));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@Valid @PathVariable UUID orderId) {
        OrderDTO dto = orderService.viewOrderDetails(orderId);
        return ResponseEntity.ok(orderService.cancelOrder(dto));
    }

    // ----------------- ORDER ITEMS -----------------

    @PostMapping("/{orderId}/items/{productId}")
    public ResponseEntity<OrderDTO> addOrderItem(@Valid @RequestBody OrderItemRequestDTO orderItemRequestDTO,
                                                 @PathVariable UUID orderId,
                                                 @PathVariable Long productId) {
        return ResponseEntity.ok(orderService.addOrderItem(orderItemRequestDTO, orderId, productId));
    }

    @DeleteMapping("/{orderId}/items/{productId}")
    public ResponseEntity<OrderDTO> deleteOrderItem(@Valid @PathVariable UUID orderId,
                                                    @PathVariable Long productId) {
        return ResponseEntity.ok(orderService.deleteOrderItem(orderId, productId));
    }

    @PutMapping("/{orderId}/items/{productId}")
    public ResponseEntity<OrderDTO> updateOrderItem(@Valid @PathVariable UUID orderId,
                                                    @PathVariable Long productId,
                                                    @RequestParam Integer quantity) {
        return ResponseEntity.ok(orderService.updateOrderItem(orderId, productId, quantity));
    }

    // ----------------- READ -----------------

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> viewOrderDetails(@Valid @PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.viewOrderDetails(orderId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> viewUserOrders(@Valid @PathVariable UUID userId,
                                                                 @RequestParam(defaultValue = "false") boolean byIdOnly) {
        if (byIdOnly) {
            return ResponseEntity.ok(orderService.viewUserOrdersById(userId));
        }
        return ResponseEntity.ok(orderService.viewUserOrders(userId));
    }

    @GetMapping("/{orderId}/status/history")
    public ResponseEntity<List<OrderStatusHistoryDTO>> viewOrderStatusHistory(@Valid @PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.viewOrderStatusHistory(orderId));
    }
}

