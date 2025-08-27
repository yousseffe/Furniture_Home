package com.furniturehome.controller;

import com.furniturehome.dto.ReviewDTO;
import com.furniturehome.model.User;
import com.furniturehome.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO){
        return ResponseEntity.ok(reviewService.createReview(reviewDTO));
    }


    @GetMapping("/product/{productid}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProduct(@PathVariable Long productid){
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productid));
    }

    @GetMapping("/user/{userid}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUser(@PathVariable Integer userid){
        return ResponseEntity.ok(reviewService.getReviewsByUser(userid));
    }

    @DeleteMapping("/delete/{reviewid}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID reviewid){
        reviewService.deleteById(reviewid);
        return ResponseEntity.noContent().build();
    }
}
