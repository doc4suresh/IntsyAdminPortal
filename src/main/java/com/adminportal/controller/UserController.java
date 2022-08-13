package com.adminportal.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.adminportal.entity.Users;
import com.adminportal.entity.security.PasswordResetToken;
import com.adminportal.entity.security.Role;
import com.adminportal.entity.security.UserRole;
import com.adminportal.service.RoleService;
import com.adminportal.service.UserService;
import com.adminportal.utility.MailConstructor;
import com.adminportal.utility.SecurityUtility;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private Environment env;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	@Autowired
	private MailConstructor mailConstructor;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private UserDetailsService userDetailsService;

	// -----------------------------Reset
	// Password-----------------------------------
	@GetMapping("/resetPassword")
	public String resetPasswordGet() {
		return "resetPassword";
	}

	@RequestMapping(value = "resetPassword", method = RequestMethod.POST)
	public String resetPasswordPost(HttpServletRequest request, @ModelAttribute("username") String username,
			Model model) throws Exception {

		Users user = userService.findByUsername(username);

		if (user == null) {
			model.addAttribute("emailNotExist", true);
			return "resetPassword";
		}

		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);
		userService.updateUser(user);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);

		String appurl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		String method = "user/newCusEmail";

		SimpleMailMessage newMessage = mailConstructor.constructorResetTokenEmail(appurl, request.getLocale(), token,
				user, encryptedPassword, method);

		mailSender.send(newMessage);

		model.addAttribute("forgotPasswordEmailSent", true);

		return "resetPassword";
	}

	// ----------Step2 of the customer registration process -- verify through email
	// link -------
	/**
	 * Email verification with Temporary Token
	 * 
	 * @param locale
	 * @param token  - token parameter from the url
	 * @param model
	 * @return
	 */
	@GetMapping("/newCusEmail")
	public String newCusEmailGet(Locale locale, @RequestParam("token") String token, Model model) {

		PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);
		if (passwordResetToken == null) {
			String message = "Invalid Token";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}

		Users user = passwordResetToken.getUser();
		String userEmail = user.getUsername();
		UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		model.addAttribute("user", user);
		model.addAttribute("pRToken", passwordResetToken);
		model.addAttribute("classActiveEdit", true);

		return "newCusEmail";
	}

	// ----------Step 3 --- user provide a new password and submit ---------------
	/**
	 * Update user information and New password with token verification.
	 * 
	 * @param request
	 * @param useremail
	 * @param token
	 * @param password
	 * @param regType
	 * @param firstName
	 * @param lastName
	 * @param mobile
	 * @param companyName
	 * @param companyRegNo
	 * @param address1
	 * @param address2
	 * @param city
	 * @param district
	 * @param comment
	 * @param model
	 * @return
	 * @throws Exception
	 */

	@PostMapping("/newCusEmail")
	public String newCusEmailPost(HttpServletRequest request, @ModelAttribute("email") String username,
			@ModelAttribute("token") String token, @ModelAttribute("newPassword") String password,

			Model model) throws Exception {

		model.addAttribute("classActiveEdit", true);
		model.addAttribute("email", username);

		PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);
		if (passwordResetToken == null) {
			String message = "Invalid Token";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}

		Users user = userService.findByUsername(username);
		if (user == null) {
			return "redirect:/badRequest";
		}

		user.setUsername(username);
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);

		userService.updateUser(user);

		model.addAttribute("userUpdated", true);

		return "dashboard";
	}

//	=====================User=================================

	@RequestMapping("/userList")
	public String userList(Model model) {

		List<Users> userList = userService.findAll();
		List<Users> systemUserList = new ArrayList<>();
		for (Users user : userList) {
			for (UserRole userrole : user.getUserRole()) {
				if (userrole.getRole().getName().equalsIgnoreCase("Admin")) {
					systemUserList.add(user);
				}

			}

		}
		model.addAttribute("systemUserList", systemUserList);

		return "userList";
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.GET)
	public String addUserGet(@RequestParam(required = false, defaultValue = "", name = "username") String username,
			Model model) {
		if (username.isEmpty()) {

			Users user = new Users();
			Role role = new Role();
			List<Role> roleList = roleService.findAll();
			List<String> roleNameList = new ArrayList<String>();
			for (Role eachrole : roleList) {
				roleNameList.add(eachrole.getName());
			}

			model.addAttribute("user", user);
			model.addAttribute("roleNameList", roleNameList);
			model.addAttribute("role", role);
			model.addAttribute("Edit", false);

			return "addUser";
		} else {

			Users user = userService.findByUsername(username);

			model.addAttribute("user", user);
			Role role = new Role();

			model.addAttribute("role", role);
			model.addAttribute("Edit", true);

			List<Role> roleList = roleService.findAll();
			List<String> roleNameList = new ArrayList<String>();
			for (Role eachrole : roleList) {
				roleNameList.add(eachrole.getName());
			}
			model.addAttribute("roleNameList", roleNameList);

			return "addUser";
		}
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public String addUserPost(@ModelAttribute("user") Users user, @RequestParam("Edit") Boolean Edit, Model model,
			HttpServletRequest request) throws Exception {

		if (Edit) {

			Users user1 = userService.findByUsername(user.getUsername());
			if (user.getUsername().equals(user1.getUsername())) {

				user1.setFirstName(user.getFirstName());
				user1.setLastName(user.getLastName());
				user1.setMobile(user.getMobile());

			}

			userService.updateUser(user1);
		}

		else {

			Users localUser = userService.findByUsername(user.getUsername());

			if (localUser != null) {
				LOG.info("user email {} already exists. Nothing will be done.", user.getUsername());

				model.addAttribute("emailExists", true);
				return "addUser";

			} else {
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

				SimpleMailMessage email = mailConstructor.constructorResetTokenEmail(appUrl, request.getLocale(), token,
						user, encryptedPassword, method);

				mailSender.send(email);
			}
		}

		return "redirect:userList";

	}

	@GetMapping("tempPassword")
	public String tempPasswordGet(Locale locale, @RequestParam("token") String token, Model model) {

		PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);
		if (passwordResetToken == null) {
			String message = "Invalid Token";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}

		Users user = passwordResetToken.getUser();
		String userEmail = user.getUsername();
		UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		model.addAttribute("user", user);
		model.addAttribute("pRToken", passwordResetToken);
		model.addAttribute("classActiveEdit", true);

		return "tempPassword";

	}

	@PostMapping("tempPassword")
	public String tempPasswordPost(Model model, @ModelAttribute("newPassword") String password,
			@ModelAttribute("username") String username) throws Exception {

		Users user1 = userService.findByUsername(username);
		if (user1 == null) {
			String message = "User cannot change password";
			model.addAttribute("message", message);

			return "redirect:/badRequest";
		}
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user1.setPassword(encryptedPassword);
		userService.updateUser(user1);
		return "dashboard";

	}
// ===========================Customer===========================

	@RequestMapping("/cusList")
	public String cusList(Model model) {

		List<Users> userList = userService.findAll();
		List<Users> customerUserList = new ArrayList<>();
		for (Users user : userList) {
			for (UserRole userrole : user.getUserRole()) {
				if (userrole.getRole().getName().equalsIgnoreCase("Customer")) {
					customerUserList.add(user);
				}

			}

		}
		model.addAttribute("customerUserList", customerUserList);

		return "cusList";
	}

	@RequestMapping(value = "/cusProfile")
	public String cusProfileGet(@RequestParam("username") String username, Model model) {

		Users user = userService.findByUsername(username);
		model.addAttribute("user", user);
		return "cusProfile";
	}

	@RequestMapping(value = "/cusApproval", method = RequestMethod.POST)
	public String cusApproval(@ModelAttribute("user") Users user, @ModelAttribute("status") String status, Model model, Principal principal,
			HttpServletRequest request) throws Exception {

		Users user1 = userService.findByUsername(user.getUsername());
		
		
		if (status.equalsIgnoreCase("Approved1") && (user1.getStatus().equalsIgnoreCase("Registered"))) {
			user.setFirstAdmin(principal.getName());
			
			userService.save(user);
			
		}
		
		if (status.equalsIgnoreCase("Approved1") && (user1.getStatus().equalsIgnoreCase("Registered"))) {
			user.setFirstAdmin(principal.getName());
			
			userService.save(user);
			
		}

		if (status.equalsIgnoreCase("Approved2") && (user1.getStatus().equalsIgnoreCase("Approved1"))) {

			if (user1.getFirstAdmin().equalsIgnoreCase(principal.getName())) {
				boolean firstApproved = true;
				model.addAttribute("firstApproved", firstApproved);
				
			} else {

				String password = SecurityUtility.randomPassword();
				String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
				user.setPassword(encryptedPassword);
				userService.updateUser(user);

				String token = UUID.randomUUID().toString();
				userService.createPasswordResetTokenForUser(user, token);

				String appUrl = "http://" + env.getProperty("support.customerportal");

				// this method(Get Mapping)will call when email clicked
				String method = "newCusEmail";

				SimpleMailMessage email = mailConstructor.constructorResetTokenEmail(appUrl, request.getLocale(), token,
						user, encryptedPassword, method);

				mailSender.send(email);

			}
		}

		return "redirect:cusList";

	}

	@GetMapping("badRequest")
	public String badRequest() {
		return "/error/badRequest";
	}

}
