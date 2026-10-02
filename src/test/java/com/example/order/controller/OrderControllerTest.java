package com.example.order.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void listIsEmptyBeforeAnyOrderIsCreated() throws Exception {
		mockMvc.perform(get("/orders"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isEmpty());
	}

	@Test
	void createOrderAssignsIdAndCreatedStatus() throws Exception {
		mockMvc.perform(post("/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"customerId":1,"productId":1,"quantity":2}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.customerId").value(1))
				.andExpect(jsonPath("$.productId").value(1))
				.andExpect(jsonPath("$.quantity").value(2))
				.andExpect(jsonPath("$.status").value("CREATED"));
	}

	@Test
	void secondOrderGetsTheNextId() throws Exception {
		String body = """
				{"customerId":1,"productId":2,"quantity":1}
				""";

		mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1));

		mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON).content(body))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(2))
				.andExpect(jsonPath("$.status").value("CREATED"));
	}

	@Test
	void listReturnsCreatedOrders() throws Exception {
		mockMvc.perform(post("/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{"customerId":4,"productId":2,"quantity":3}
								"""))
				.andExpect(status().isOk());

		mockMvc.perform(get("/orders"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].customerId").value(4))
				.andExpect(jsonPath("$[0].status").value("CREATED"));
	}
}
