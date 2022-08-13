package com.adminportal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.entity.Subcategory;
import com.adminportal.repository.SubcategoryRepository;
import com.adminportal.service.SubcategoryService;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {

	@Autowired
	private SubcategoryRepository subcategoryRepository; 
	
	@Override
	public List<Subcategory> findAll() {
		
		return subcategoryRepository.findAll();
	}

	@Override
	public Subcategory Save(Subcategory subcategory) {
		return subcategoryRepository.save(subcategory);
	}

	@Override
	public Subcategory findById(Long id) {
		return subcategoryRepository.getOne(id);
	}

	@Override
	public Subcategory findBySubcatName(String subcatName) {
		return subcategoryRepository.findBySubcatName(subcatName);
	}

	@Override
	public void save(Subcategory subcategoryNew) {
		subcategoryRepository.save(subcategoryNew)	;	
	}

	@Override
	public void delete(Subcategory subcategory) {
		subcategoryRepository.delete(subcategory);		
	}

}
