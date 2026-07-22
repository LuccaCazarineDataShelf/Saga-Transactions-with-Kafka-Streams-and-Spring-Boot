package org.example.api.dto;

public record CreateOrderResponseDTO(
        String orderId,
        String orderStatus
) {
}
