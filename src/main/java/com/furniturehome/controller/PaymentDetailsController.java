package com.furniturehome.controller;

import com.furniturehome.dto.PaymentDetailsDTO;
import com.furniturehome.model.PaymentDetails;
import com.furniturehome.service.PaymentDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/payment")
public class PaymentDetailsController {
    private final PaymentDetailsService paymentDetailsService;

    @PostMapping
    public ResponseEntity<PaymentDetailsDTO> createPayment(@RequestBody PaymentDetailsDTO dto){
        return ResponseEntity.ok(paymentDetailsService.createPayment(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDetailsDTO> getPaymentById(@PathVariable UUID id){
        return ResponseEntity.ok(paymentDetailsService.getPaymentById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaymentDetailsDTO>> getAllPayments(){
        return ResponseEntity.ok(paymentDetailsService.getAllPayments());
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<PaymentDetailsDTO>updatePayment(@PathVariable UUID id, @RequestBody PaymentDetailsDTO dto){
        return ResponseEntity.ok(paymentDetailsService.updatePayment(id, dto));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void>deleteById(@PathVariable UUID id){
        paymentDetailsService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

}
