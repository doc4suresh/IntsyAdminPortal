package com.adminportal.service;

import java.util.List;


import com.adminportal.entity.Item;



public interface ItemService {
	
	Item save(Item item);
	

	
	Item findOne(Long id);

	List<Item> findAll();

	void update(Item item);
	
	public List<String> search(String keyword);

	Item findByItemCode(String itemCode);

	
	

}
