package com.modsen.mapper;

import com.modsen.dto.payment.PaymentRequest;
import com.modsen.dto.payment.PaymentResponse;
import com.modsen.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PaymentMapper {

    PaymentMapper MAPPER_INSTANCE = Mappers.getMapper(PaymentMapper.class);

    PaymentResponse mapToPaymentResponse(Payment payment);

    List<PaymentResponse> mapToListOfPaymentResponse(List<Payment> payments);

    Payment mapToPayment(PaymentRequest paymentRequest);
}