package com.adminportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adminportal.entity.Subcategory;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {

	Subcategory findBySubcatName(String subcatName);
	
	
	
	
}
