package com.adminportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.adminportal.entity.CartItem;
import com.adminportal.entity.ItemToCartItem;


@Transactional
public interface ItemToCartItemRepository extends JpaRepository<ItemToCartItem, Long> {
	void deleteByCartItem(CartItem cartItem);
}
