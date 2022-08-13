package com.adminportal.service;

import java.util.List;

import org.springframework.core.env.Environment;

import com.adminportal.entity.CartItem;
import com.adminportal.entity.Item;
import com.adminportal.entity.Order;
import com.adminportal.entity.ShoppingCart;
import com.adminportal.entity.Users;





public interface CartItemService {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	CartItem updateCartItem(CartItem cartItem, Users user, Environment env);
	
	CartItem addItemToCartItem(Item item, Users user, int qty, Environment env);
	
	CartItem findById(Long id);
	
	void removeFromCartItem(CartItem cartItem);
	
	CartItem save(CartItem cartItem);
	
	List<CartItem> findByOrder(Order order);
	
	List<CartItem> findByItem(Item item);
}
