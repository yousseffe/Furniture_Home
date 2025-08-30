package com.furniturehome.controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.furniturehome.dto.EnquiryDTO;
import com.furniturehome.service.EnquiryService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/enquiries")
public class EnquiryController {

    @Autowired
    private EnquiryService enquiryService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public EnquiryDTO createEnquiry(@RequestBody EnquiryDTO enquiryDTO) {
        return enquiryService.createEnquiry(enquiryDTO);
    }

    // @PreAuthorize("hasRole('ADMIN')")
    // @GetMapping
    // public List<EnquiryDTO> getAllEnquiries() {
    //     return enquiryService.getAllEnquiries();
    // }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/user/{userId}")
    public List<EnquiryDTO> getEnquiriesByUser(@PathVariable UUID userId) {
        return enquiryService.getEnquiriesByUserId(userId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping("/{id}")
    public EnquiryDTO getEnquiryById(@PathVariable UUID id) {
        return enquiryService.getEnquiryById(id);
    }

    // @PreAuthorize("hasRole('CUSTOMER')")
    // @PutMapping("/{id}")
    // public EnquiryDTO updateEnquiry(@PathVariable UUID id, @RequestBody EnquiryDTO dto) {
    //     return enquiryService.updateEnquiry(id, dto);
    // }

    // @PreAuthorize("hasRole('CUSTOMER')")
    // @DeleteMapping("/{id}")
    // public void deleteEnquiry(@PathVariable UUID id) {
    //     enquiryService.deleteEnquiry(id);
    // }
}
