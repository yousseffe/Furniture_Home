package com.furniturehome.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnquiryDTO {
    private UUID enquiryId;
    private String content;
    private UUID userId;
    private LocalDateTime createdAt;
    
    // --- Getters and Setters ---
    public UUID getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(UUID enquiryId) {
        this.enquiryId = enquiryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
