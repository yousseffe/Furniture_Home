package com.furniturehome.service;

import org.springframework.stereotype.Service;

import com.furniturehome.dto.EnquiryDTO;
import com.furniturehome.model.Enquiry;
import com.furniturehome.model.User;
import com.furniturehome.repository.EnquiryRepository;
import com.furniturehome.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EnquiryServiceImpl implements EnquiryService {

    @Autowired
    private EnquiryRepository enquiryRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public EnquiryDTO createEnquiry(EnquiryDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Enquiry enquiry = new Enquiry();
        enquiry.setContent(dto.getContent());
        enquiry.setUser(user);
        enquiry.setCreatedAt(LocalDateTime.now());

        enquiryRepository.save(enquiry);

        dto.setEnquiryId(enquiry.getEnquiryId());
        dto.setCreatedAt(enquiry.getCreatedAt());
        return dto;
    }

    @Override
    public List<EnquiryDTO> getEnquiriesByUserId(UUID userId) {
        return (List<EnquiryDTO>) enquiryRepository.findByUserId(userId)
                .stream()
                .map(e -> EnquiryDTO.builder()
                        .enquiryId(e.getEnquiryId())
                        .content(e.getContent())
                        .userId(e.getUser().getId())   
                        .createdAt(e.getCreatedAt())
                        .build()
                ).toList();
    }

    @Override
    public EnquiryDTO getEnquiryById(UUID enquiryId) {
        Enquiry enquiry = enquiryRepository.findById(enquiryId)
                .orElseThrow(() -> new RuntimeException("Enquiry not found"));
        EnquiryDTO dto = new EnquiryDTO();
        dto.setEnquiryId(enquiry.getEnquiryId());
        dto.setContent(enquiry.getContent());
        dto.setUserId(enquiry.getUser().getId());
        dto.setCreatedAt(enquiry.getCreatedAt());
        return dto;
    }
}

