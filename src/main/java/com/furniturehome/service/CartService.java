package com.furniturehome.service;

import com.furniturehome.dto.CartDTO;
import com.furniturehome.dto.CartItemDTO;
import com.furniturehome.model.Cart;
import com.furniturehome.model.CartItem;
import com.furniturehome.model.Product;
import com.furniturehome.model.User;
import com.furniturehome.repository.CartItemRepository;
import com.furniturehome.repository.CartRepository;
import com.furniturehome.repository.ProductRepository;
import com.furniturehome.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartDTO getCartByUserId(UUID userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCartForUser(userId));
        return convertToDTO(cart);
    }

    public CartDTO addItemToCart(UUID userId, Long productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCartForUser(userId));

        CartItem existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cartItemRepository.save(newItem);
        }

        return convertToDTO(cart);
    }

    public CartDTO updateCartItemQuantity(UUID userId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        return convertToDTO(cart);
    }

    public CartDTO removeItemFromCart(UUID userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);

        return convertToDTO(cart);
    }

    public void clearCart(UUID userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cartItemRepository.deleteByCartId(cart.getId());
    }

    private Cart createCartForUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = Cart.builder()
                .user(user)
                .build();

        return cartRepository.save(cart);
    }

    private CartDTO convertToDTO(Cart cart) {
        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(this::convertCartItemToDTO)
                .collect(Collectors.toList());

        return CartDTO.builder()
                .id(cart.getId())
                .userid(cart.getUser().getId())
                .items(itemDTOs)
                .totalPrice(cart.getTotalPrice())
                .totalItems(cart.getTotalItems())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    private CartItemDTO convertCartItemToDTO(CartItem cartItem) {
        Product product = cartItem.getProduct();
        String productImage = product.getImages() != null && !product.getImages().isEmpty()
                ? product.getImages().get(0).getImgUrl()
                : null;

        return CartItemDTO.builder()
                .id(cartItem.getId())
                .cartId(cartItem.getCart().getId())
                .productId(product.getId())
                .productName(product.getName())
                .productDescription(product.getDescription())
                .productPrice(product.getPrice())
                .productImage(productImage)
                .quantity(cartItem.getQuantity())
                .totalPrice(product.getPrice() * cartItem.getQuantity())
                .createdAt(cartItem.getCreatedAt())
                .updatedAt(cartItem.getUpdatedAt())
                .build();
    }
}
