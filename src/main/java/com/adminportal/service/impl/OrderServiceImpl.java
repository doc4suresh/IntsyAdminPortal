package com.adminportal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.entity.Order;
import com.adminportal.repository.OrderRepository;
import com.adminportal.service.OrderService;


@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository orderRepository;
	
	public Order findOne(Long id) {
		return orderRepository.getOne(id);
	}
	
	public List<Order> findAll(){
		return orderRepository.findAll();
		
	}

	@Override
	public void save(Order order) {
		orderRepository.save(order);
	}

}
