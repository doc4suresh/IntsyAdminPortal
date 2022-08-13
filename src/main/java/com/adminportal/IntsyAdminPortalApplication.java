package com.adminportal;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.adminportal.entity.Users;
import com.adminportal.entity.security.Role;
import com.adminportal.entity.security.UserRole;
import com.adminportal.service.UserService;
import com.adminportal.utility.SecurityUtility;

@SpringBootApplication
public class IntsyAdminPortalApplication implements CommandLineRunner{

	@Autowired
	private UserService userService;
	
	public static void main(String[] args) {
		SpringApplication.run(IntsyAdminPortalApplication.class, args);
	}
	
	
	public void run(String... args) throws Exception {
		Users user1 = new Users();
		user1.setFirstName("Dilusha");
		user1.setLastName("Alwis");
		user1.setUsername("admin@gmail.com");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("123"));
		Set<UserRole> userRoles = new HashSet<>();
		Role role1= new Role();
		role1.setName("Admin");
		userRoles.add(new UserRole(user1, role1));
		
		userService.createUser(user1, userRoles);
	}

}
