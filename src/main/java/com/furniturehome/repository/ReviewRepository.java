package com.furniturehome.repository;

import com.furniturehome.dto.ReviewDTO;
import com.furniturehome.model.Product;
import com.furniturehome.model.Review;
import com.furniturehome.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByProduct(Product product);
    List<Review> findByUser(User user);
}
