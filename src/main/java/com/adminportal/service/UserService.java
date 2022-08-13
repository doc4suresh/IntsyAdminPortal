package com.adminportal.service;

import java.util.List;
import java.util.Set;

import com.adminportal.entity.Users;
import com.adminportal.entity.security.PasswordResetToken;
import com.adminportal.entity.security.UserRole;

public interface UserService {
	
	PasswordResetToken getPasswordResetToken( final String token);
	
	void createPasswordResetTokenForUser(final Users user, final String token);
	
	Users findByUsername(String username);
	
//	List<User> findByUserRole(String role);
	
	Users createUser (Users user, Set<UserRole> userRoles) throws Exception;
	
	Users updateUser (Users user) throws Exception;
	
	List<Users> findAll();

	List<Users> findByStatus(String status);

	List<Users> findByRegType(String string);

	void save(Users user);

//	Role findById(Long id);
	
}
