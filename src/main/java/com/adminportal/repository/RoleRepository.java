package com.adminportal.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.adminportal.entity.security.Role;

public interface RoleRepository extends CrudRepository<Role,  Long>{
	
	Role findByName(String name);
	
	Optional<Role> findById(Long id);
}
