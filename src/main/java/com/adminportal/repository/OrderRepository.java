package com.adminportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adminportal.entity.Order;



public interface OrderRepository extends JpaRepository<Order, Long>{

}
