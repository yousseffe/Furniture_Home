package com.furniturehome.repository;

import com.furniturehome.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Integer userId);
    Optional<Favorite> findByUserIdAndProductId(Integer userId, Long productId);
    boolean existsByUserIdAndProductId(Integer userId, Long productId);
    void deleteByUserIdAndProductId(Integer userId, Long productId);
}
