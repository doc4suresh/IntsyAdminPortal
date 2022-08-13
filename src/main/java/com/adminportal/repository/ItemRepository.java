package com.adminportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.adminportal.entity.Item;



public interface ItemRepository extends JpaRepository<Item, Long> {
	
	/*
	 * @Query("SELECT itemCode FROM Item where itemCode like %:keyword%") public
	 * List<String> search(@Param("keyword") String keyword);
	 */
	
	Item findByItemCode(String itemCode);
}
