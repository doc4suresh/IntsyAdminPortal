package com.adminportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.adminportal.entity.CartItem;
import com.adminportal.entity.Item;
import com.adminportal.entity.Order;
import com.adminportal.entity.ShoppingCart;



@Transactional
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	List<CartItem> findByOrder(Order order);

	List<CartItem> findByItem(Item item);
}
