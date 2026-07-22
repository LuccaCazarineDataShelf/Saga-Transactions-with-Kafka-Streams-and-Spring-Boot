package org.example.api.controller;

import org.example.api.dto.CreateOrderRequestDTO;
import org.example.api.dto.CreateOrderResponseDTO;
import org.example.application.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponseDTO createOrder(@RequestBody CreateOrderRequestDTO createOrderRequestDTO){
        return orderService.createOrder(createOrderRequestDTO);
    }
}
