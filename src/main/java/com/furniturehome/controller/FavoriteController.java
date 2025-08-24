package com.furniturehome.controller;

import com.furniturehome.dto.FavoriteDTO;
import com.furniturehome.dto.FavoriteRequestDTO;
import com.furniturehome.service.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<FavoriteDTO>> getUserFavorites(@PathVariable Integer userId) {
        try {
            List<FavoriteDTO> favorites = favoriteService.getUserFavorites(userId);
            return ResponseEntity.ok(favorites);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<FavoriteDTO> addToFavorites(
            @PathVariable Integer userId,
            @Valid @RequestBody FavoriteRequestDTO request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            FavoriteDTO favorite = favoriteService.addToFavorites(userId, request.getProductId());
            return ResponseEntity.ok(favorite);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeFromFavorites(
            @PathVariable Integer userId,
            @Valid @RequestBody FavoriteRequestDTO request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            favoriteService.removeFromFavorites(userId, request.getProductId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{userId}/check")
    public ResponseEntity<Boolean> isProductInFavorites(
            @PathVariable Integer userId,
            @RequestParam Long productId) {
        try {
            boolean isFavorite = favoriteService.isProductInFavorites(userId, productId);
            return ResponseEntity.ok(isFavorite);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
