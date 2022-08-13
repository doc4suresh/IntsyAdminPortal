package com.adminportal.service;

import java.util.List;

import com.adminportal.entity.security.Role;

public interface RoleService {
	
	List<Role> findAll();

	Role findByName(String roleName);
	

}
