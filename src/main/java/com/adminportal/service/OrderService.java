package com.adminportal.service;

import java.util.List;

import com.adminportal.entity.Order;

public interface OrderService {

	
	Order findOne(Long id);
	
	List<Order> findAll();

	void save(Order order);
}
