package org.example.api.dto;

public record CreateOrderRequestDTO(
        String customerId,
        String assetCode,
        String side,
        Integer quantity,
        Long unitPriceCents
) {
}
