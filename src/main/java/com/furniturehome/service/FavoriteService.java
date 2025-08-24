package com.furniturehome.service;

import com.furniturehome.dto.FavoriteDTO;
import com.furniturehome.model.Favorite;
import com.furniturehome.model.Product;
import com.furniturehome.model.User;
import com.furniturehome.repository.FavoriteRepository;
import com.furniturehome.repository.ProductRepository;
import com.furniturehome.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<FavoriteDTO> getUserFavorites(Integer userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        return favorites.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FavoriteDTO addToFavorites(Integer userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if already in favorites
        if (favoriteRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new RuntimeException("Product is already in favorites");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .product(product)
                .build();

        Favorite savedFavorite = favoriteRepository.save(favorite);
        return convertToDTO(savedFavorite);
    }

    public void removeFromFavorites(Integer userId, Long productId) {
        favoriteRepository.deleteByUserIdAndProductId(userId, productId);
    }

    public boolean isProductInFavorites(Integer userId, Long productId) {
        return favoriteRepository.existsByUserIdAndProductId(userId, productId);
    }

    private FavoriteDTO convertToDTO(Favorite favorite) {
        Product product = favorite.getProduct();
        String productImage = product.getImages() != null && !product.getImages().isEmpty() 
                ? product.getImages().get(0).getImgUrl() 
                : null;

        return FavoriteDTO.builder()
                .id(favorite.getId())
                .userId(favorite.getUser().getId())
                .productId(product.getId())
                .productName(product.getName())
                .productDescription(product.getDescription())
                .productPrice(product.getPrice())
                .productImage(productImage)
                .createdAt(favorite.getCreatedAt())
                .build();
    }
}
