package com.adminportal.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.adminportal.entity.Users;

public interface UserRepository extends CrudRepository< Users, Long> {
	
	Users findByUsername(String username);

	List<Users> findByRegType(String string);
	
	//List<User> findByUserRole(String role);
	
}
