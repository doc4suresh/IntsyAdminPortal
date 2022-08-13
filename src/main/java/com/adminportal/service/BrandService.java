package com.adminportal.service;

import java.util.List;

import com.adminportal.entity.Brand;

public interface BrandService {

	List<Brand> findAll();
	
	Brand save(Brand brand);
	
	public Void delete(Brand brand);

	Brand findByBrandName(String brandName);

	Brand findById(Long id);

}
