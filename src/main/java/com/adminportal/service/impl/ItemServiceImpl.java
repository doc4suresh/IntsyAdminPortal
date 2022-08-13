package com.adminportal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.entity.Item;
import com.adminportal.repository.ItemRepository;
import com.adminportal.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService{
	
	@Autowired
	private ItemRepository itemRepository;

	@Override
	public Item save(Item item) {
		
		return itemRepository.save(item);
	}

//	@SuppressWarnings("unchecked")
//	public List<Item> findAll(int page) {
//		
//		Pageable pageable = PageRequest.of(page, 10);
//		return (List<Item>) itemRepository.findAll(pageable);
//	}

	@Override
	public Item findOne(Long id) {
		
		return itemRepository.getOne(id);
	}

	@Override
	public List<Item> findAll() {
			return itemRepository.findAll();
	}


	@Override
	public void update(Item item) {

		
	}

	/*
	 * @Override public List<String> search(String keyword) { return
	 * itemRepository.search(keyword); }
	 */

	@Override
	public List<String> search(String keyword) {
		List<Item> itemList = itemRepository.findAll();
		List<String> itemCodeList = new ArrayList<>();
		for(Item item : itemList) {
			if(item.getItemCode().contains(keyword)) {
				itemCodeList.add(item.getItemCode());
			}
		}
		return itemCodeList;
	}

	@Override
	public Item findByItemCode(String itemCode) {
		// TODO Auto-generated method stub
		return itemRepository.findByItemCode(itemCode);
	}

}
