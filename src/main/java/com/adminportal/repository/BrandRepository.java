package com.adminportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.adminportal.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

	Brand findByBrandName(String brandName);

}
