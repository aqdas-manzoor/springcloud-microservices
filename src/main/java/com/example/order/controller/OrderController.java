package com.example.order.controller;

import com.example.order.model.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final List<Order> orders = new ArrayList<>();

	@GetMapping
	public List<Order> getOrders() {
		return orders;
	}

	@PostMapping
	public Order createOrder(@RequestBody Order order) {
		order.setId((long) (orders.size() + 1));
		order.setStatus("CREATED");
		orders.add(order);
		return order;
	}
}
