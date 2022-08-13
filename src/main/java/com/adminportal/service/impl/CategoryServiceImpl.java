package com.adminportal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.entity.Category;
import com.adminportal.repository.CategoryRepository;
import com.adminportal.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Category save(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public Category findById(Long id) {
		return categoryRepository.getOne(id);
	}

	@Override
	public Category findByCatName(String catName) {
	
		return categoryRepository.findByCatName(catName);
	}

	@Override
	public void delete(Category category) {
		categoryRepository.delete(category);
		
	}
	
	
	
	
}
