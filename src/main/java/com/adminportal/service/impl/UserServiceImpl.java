package com.adminportal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.entity.Users;
import com.adminportal.entity.security.PasswordResetToken;
import com.adminportal.entity.security.UserRole;
import com.adminportal.repository.PasswordResetTokenRepository;
import com.adminportal.repository.RoleRepository;
import com.adminportal.repository.UserRepository;
import com.adminportal.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public PasswordResetToken getPasswordResetToken(String token) {	
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetTokenForUser(Users user, String token) {
		PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(passwordResetToken);
	}

	
	@Override
	public Users findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public Users createUser(Users user, Set<UserRole> userRoles) {
		Users localUser = userRepository.findByUsername(user.getUsername());
		
		if(localUser != null) {
			LOG.info("user email {} already exists. Nothing will be done.", user.getUsername());
		}else {
			for(UserRole ur : userRoles) {
				roleRepository.save(ur.getRole());
			}
			
			user.getUserRole().addAll(userRoles);
		
			localUser = userRepository.save(user);
			
		}
		
		return localUser;
	}

	@Override
	public Users updateUser(Users user) {
		Users localUser = userRepository.findByUsername(user.getUsername());
		
		if(localUser == null) {
			LOG.info("user email {} already exists. Nothing will be done.", user.getUsername());
		}else {
			userRepository.save(user);
			
		}
				
		return localUser;
	}
	
	@Override
	public List<Users> findAll(){
		
		return (List<Users>) userRepository.findAll();
						
	}

	@Override
	public List<Users> findByStatus(String status) {
		Iterable<Users> userList = userRepository.findAll();
		List<Users> userListStatus = new ArrayList<>();
		for(Users user: userList) {
			
			if (user.getStatus() != null && user.getStatus().equals(status)){
				userListStatus.add(user);
			}
		}
		return userListStatus;
	}

	@Override
	public List<Users> findByRegType(String string) {
		return userRepository.findByRegType(string);
	}

	@Override
	public void save(Users user) {
		userRepository.save(user);
		
	}



//	@Override
//	public List<User> findByUserRole(String role) {
//		
//		return userRepository.findByUserRole(role);
//	}
//	
	
	
}
