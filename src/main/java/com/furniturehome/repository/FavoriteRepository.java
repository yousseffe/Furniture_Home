package com.furniturehome.repository;

import com.furniturehome.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(UUID userId);
    Optional<Favorite> findByUserIdAndProductId(UUID userId, Long productId);
    boolean existsByUserIdAndProductId(UUID userId, Long productId);
    void deleteByUserIdAndProductId(UUID userId, Long productId);
}
