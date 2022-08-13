package com.adminportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adminportal.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

	Category findByCatName(String catName);


}
