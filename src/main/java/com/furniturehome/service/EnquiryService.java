package com.furniturehome.service;

import java.util.List;
import java.util.UUID;

import com.furniturehome.dto.EnquiryDTO;

public interface EnquiryService {
    EnquiryDTO createEnquiry(EnquiryDTO enquiryDTO);
    List<EnquiryDTO> getEnquiriesByUserId(UUID userId);
    EnquiryDTO getEnquiryById(UUID id);
}

