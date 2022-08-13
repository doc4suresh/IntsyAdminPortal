package com.adminportal.service;

import java.util.List;

import com.adminportal.entity.Category;

public interface CategoryService {
	
	List<Category> findAll();
	
	Category save(Category category);

	Category findById(Long id);

	Category findByCatName(String catName);

	void delete(Category category);

}
