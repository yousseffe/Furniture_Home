package com.furniturehome.service;

import com.furniturehome.dto.ReviewDTO;
import com.furniturehome.model.Product;
import com.furniturehome.model.Review;
import com.furniturehome.model.User;
import com.furniturehome.repository.ProductRepository;
import com.furniturehome.repository.ReviewRepository;
import com.furniturehome.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private Review convertToEntity(ReviewDTO reviewDTO, User user, Product product){
        Review review = new Review();
        review.setReview_id(reviewDTO.getReview_id());
        review.setUser(user);
        review.setProduct(product);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setCreated_at(reviewDTO.getCreated_at());
        return review;
    }

    private ReviewDTO convertToDTO(Review review){
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setReview_id(review.getReview_id());
        reviewDTO.setUserid(review.getUser().getId());
        reviewDTO.setProduct_id(review.getProduct().getId());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setCreated_at(review.getCreated_at());
        return reviewDTO;
    }

    public ReviewDTO createReview(ReviewDTO reviewDTO){
        Product product = productRepository.findById(reviewDTO.getProduct_id())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepository.findById(reviewDTO.getUserid())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = convertToEntity(reviewDTO, user,product);
        return convertToDTO(reviewRepository.save(review));
    }

    public List<ReviewDTO> getReviewsByProduct(Long id){
        Product product = productRepository.findById(id).
                orElseThrow(()->new RuntimeException("Product not found"));

        return reviewRepository.findByProduct(product)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByUser(UUID id){
        User user = userRepository.findById(id).
                orElseThrow(()->new RuntimeException("User not found"));

        return reviewRepository.findByUser(user)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(UUID reviewId){
        reviewRepository.deleteById(reviewId);
    }
}

