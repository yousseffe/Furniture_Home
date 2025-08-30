package com.furniturehome.repository;

import com.furniturehome.model.Enquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface EnquiryRepository extends JpaRepository<Enquiry, UUID> {
    List<Enquiry> findByUserId(UUID userId);
}
