package org.example.application;

import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import org.example.api.dto.CreateOrderRequestDTO;
import org.example.api.dto.CreateOrderResponseDTO;
import org.example.eventes.OrderCreated;
import org.example.eventes.OrderSide;
import org.example.eventes.SagaEventInfo;
import org.example.kafka.OrderEventProducer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {
    private final OrderEventProducer orderEventProducer;

    public OrderService(OrderEventProducer orderEventProducer) {
        this.orderEventProducer = orderEventProducer;
    }

    public CreateOrderResponseDTO createOrder(CreateOrderRequestDTO request) {
        String orderId = UUID.randomUUID().toString();
        String eventId = UUID.randomUUID().toString();

        long totalAmountInCents = request.quantity() * request.unitPriceCents();

        OrderCreated orderCreated = OrderCreated.newBuilder()
                .setOrderId(orderId)
                .setCustomerId(request.customerId())
                .setAssetCode(request.assetCode())
                .setOrderSide(verifyAndParseOrderSide(request.side()))
                .setQuantity(request.quantity())
                .setUnitPriceCents(request.unitPriceCents())
                .setTotalPriceCents(totalAmountInCents)
                .build();

        SagaEventInfo sagaEventInfo = SagaEventInfo.newBuilder()
                .setEventId(eventId)
                .setSagaId(orderId)
                .setOccurredAt(Timestamps.fromMillis(System.currentTimeMillis()))
                .setOrderCreated(orderCreated)
                .build();

        orderEventProducer.publishOrderCreated(sagaEventInfo);

        return new CreateOrderResponseDTO(orderId, "PENDING");
    }

    private OrderSide verifyAndParseOrderSide(String orderSide) {
        if(orderSide == null){
            return OrderSide.ORDER_SIDE_UNSPECIFIED;
        }

        return switch(orderSide.toUpperCase()){
            case "BUY" -> OrderSide.BUY;
            case "SELL" -> OrderSide.SELL;
            default -> OrderSide.ORDER_SIDE_UNSPECIFIED;
        };
    }
}
