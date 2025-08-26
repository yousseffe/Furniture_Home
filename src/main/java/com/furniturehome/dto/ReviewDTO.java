package com.furniturehome.dto;

import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
    private UUID review_id;
    private Long product_id;
    private Integer user_id;
    private String comment;
    private Integer rating;
    private LocalDateTime created_at;
}
