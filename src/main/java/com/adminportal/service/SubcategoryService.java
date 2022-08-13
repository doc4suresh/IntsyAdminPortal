package com.adminportal.service;

import java.util.List;

import com.adminportal.entity.Subcategory;

public interface SubcategoryService {
	
	List<Subcategory> findAll();
	
	Subcategory Save(Subcategory subcategory);

	Subcategory findById(Long id);

	Subcategory findBySubcatName(String subcatName);

	void save(Subcategory subcategoryNew);

	void delete(Subcategory subcategory);

}
