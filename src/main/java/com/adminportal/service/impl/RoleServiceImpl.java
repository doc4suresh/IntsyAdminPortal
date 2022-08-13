package com.adminportal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.entity.security.Role;
import com.adminportal.repository.RoleRepository;
import com.adminportal.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public List<Role> findAll(){
		
		return (List<Role>) roleRepository.findAll();
	}

	@Override
	public Role findByName(String roleName) {
		
		return roleRepository.findByName(roleName);
	}

}
