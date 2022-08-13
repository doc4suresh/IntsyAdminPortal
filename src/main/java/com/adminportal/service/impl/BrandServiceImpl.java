package com.adminportal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.entity.Brand;
import com.adminportal.repository.BrandRepository;
import com.adminportal.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandRepository brandRepository;

	@Override
	public List<Brand> findAll() {
		return brandRepository.findAll();
	}

	@Override
	public Brand save(Brand brand) {

		return brandRepository.save(brand);
	}

	@Override
	public Void delete(Brand brand) {
		brandRepository.delete(brand);
		return null;
	}

	@Override
	public Brand findByBrandName(String brandName) {
		return brandRepository.findByBrandName(brandName);
	}

	@Override
	public Brand findById(Long id) {
		return brandRepository.getOne(id);
	}

}
