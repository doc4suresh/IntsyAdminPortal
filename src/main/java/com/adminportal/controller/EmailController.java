package com.adminportal.controller;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adminportal.entity.Users;
import com.adminportal.entity.security.Role;
import com.adminportal.entity.security.UserRole;
import com.adminportal.service.RoleService;
import com.adminportal.service.UserService;
import com.adminportal.utility.MailConstructor;
import com.adminportal.utility.SecurityUtility;

@Controller
public class EmailController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private MailConstructor mailConstructor;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private MailSender mailSender;

	@GetMapping("emailSpecial")
	private String emailSpecialGet() {

		return "sendEmail";
	}

	@PostMapping("emailSpecial")
	private String emailSpecialPost(Model model, Users user, Locale locale,String contextPath,
			HttpServletRequest request) throws Exception  {
		
		Users localUser = userService.findByUsername(user.getUsername());

		if (localUser != null) {
			LOG.info("user email {} already exists. Nothing will be done.", user.getUsername());

			model.addAttribute("emailExists", true);
			return "addUser";
			
		}
		
		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);
		Set<UserRole> userRoles = new HashSet<>();
		Role role = roleService.findByName("Admin");
		userRoles.add(new UserRole(user, role));

		userService.createUser(user, userRoles);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);

		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();

		String method = "user/tempPassword";

		SimpleMailMessage email = mailConstructor.emailSpeical(contextPath, locale, user, encryptedPassword, method);
		
//		SimpleMailMessage email = mailConstructor.constructorResetTokenEmail(appUrl, request.getLocale(), token,
//				user, encryptedPassword, method);

		mailSender.send(email);
		
		
		
		return "sendEmail";
	}
		
}
