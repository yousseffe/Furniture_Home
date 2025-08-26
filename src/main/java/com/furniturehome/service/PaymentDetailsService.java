package com.furniturehome.service;

import com.furniturehome.dto.PaymentDetailsDTO;
import com.furniturehome.model.PaymentDetails;
import com.furniturehome.repository.OrderRepository;
import com.furniturehome.repository.PaymentDetailsRepository;
import lombok.RequiredArgsConstructor;
import com.furniturehome.model.Order;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentDetailsService {
    private final PaymentDetailsRepository paymentDetailsRepository;
    private final OrderRepository orderRepository;

    private PaymentDetailsDTO convertToDTO(PaymentDetails paymentDetails){
        PaymentDetailsDTO paymentDetailsDTO = new PaymentDetailsDTO();
        paymentDetailsDTO.setId(paymentDetails.getId());
        paymentDetailsDTO.setPaymentMethod(paymentDetails.getMethod());
        paymentDetailsDTO.setPaymentStatus(paymentDetails.getStatus());
        paymentDetailsDTO.setAmount(paymentDetails.getAmount());
        paymentDetailsDTO.setOrderid(paymentDetails.getOrder().getId());
        return paymentDetailsDTO;
    }

    private PaymentDetails convertToEntity(PaymentDetailsDTO paymentDetailsDTO, Order order){
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setId(paymentDetailsDTO.getId());
        paymentDetails.setAmount(paymentDetailsDTO.getAmount());
        paymentDetails.setOrder(order);
        paymentDetails.setStatus(paymentDetailsDTO.getPaymentStatus());
        paymentDetails.setMethod(paymentDetailsDTO.getPaymentMethod());
        return paymentDetails;
    }

    public PaymentDetailsDTO createPayment(PaymentDetailsDTO dto){
        Order order = orderRepository.findById(dto.getOrderid()).orElseThrow(()->new RuntimeException("Order Not Found"));
        PaymentDetails paymentDetails = convertToEntity(dto, order);
        return convertToDTO(paymentDetailsRepository.save(paymentDetails));
    }

    public List<PaymentDetailsDTO> getAllPayments(){
        return paymentDetailsRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PaymentDetailsDTO getPaymentById(UUID id){
        return paymentDetailsRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(()->new RuntimeException("Not found"));
    }


    public PaymentDetailsDTO updatePayment(UUID id, PaymentDetailsDTO dto){
        PaymentDetails paymentDetails = paymentDetailsRepository.findById(dto.getId())
                .orElseThrow(()->new RuntimeException("Not found"));

        Order order = orderRepository.findById(dto.getOrderid()).
                orElseThrow(()->new RuntimeException("Order Not Found"));

        paymentDetails.setMethod(dto.getPaymentMethod());
        paymentDetails.setStatus(dto.getPaymentStatus());
        paymentDetails.setAmount(dto.getAmount());
        return convertToDTO(paymentDetailsRepository.save(paymentDetails));
    }


    public void deletePayment(UUID id){
        if (!paymentDetailsRepository.existsById(id)){
            throw new RuntimeException("Payment Not Found");
        }
        paymentDetailsRepository.deleteById(id);
    }
}