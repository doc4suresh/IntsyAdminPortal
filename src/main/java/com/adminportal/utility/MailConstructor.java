package com.adminportal.utility;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.adminportal.entity.Users;

@Component
public class MailConstructor {
	
	@Autowired
	private Environment env;
	
	
	public SimpleMailMessage constructorResetTokenEmail (String contextPath, Locale locale, String token, Users user, String password, String method) {
		
		//String url = contextPath + "/newCusEmail?token=" + token;
		
		String url = contextPath + "/"+method+"?token=" + token;

		
		String message = "\nPlease click on this link to verify your email and edit your personal information. Your Token is : \n" + token;
		
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getUsername());
		email.setSubject("Intsy Pvt Ltd : User Verification");
		email.setText(url + message);
		email.setFrom(env.getProperty("support.email"));
		
		return email;
	}
		
		public SimpleMailMessage emailSpeical (String contextPath, Locale locale,  Users user, String password, String method) {
			
						
			String url = contextPath;

			
			String message = "\nPlease click on this link to verify your email and edit your personal information. Your Token is : \n" ;
			
			SimpleMailMessage email = new SimpleMailMessage();
			email.setTo(user.getUsername());
			email.setSubject("Intsy Pvt Ltd : User Verification");
			email.setText(url + message);
			email.setFrom(env.getProperty("support.email"));
			
		return email;
		
	}
	

	
}
