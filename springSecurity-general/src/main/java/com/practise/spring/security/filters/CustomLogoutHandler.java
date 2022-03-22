package com.practise.spring.security.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.practise.spring.security.models.CustomAuthUserDetails;
import com.practise.spring.security.repositories.ICustomUserDetailsRepository;

@Configuration
public class CustomLogoutHandler implements LogoutHandler {

	@Autowired
	private ICustomUserDetailsRepository userAuthRepository;
	
	private final Logger logger  = LoggerFactory.getLogger(CustomLogoutHandler.class);
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		
		// clear the jwt from DB 
		String username = authentication.getName();
		CustomAuthUserDetails user = (CustomAuthUserDetails) userAuthRepository.findByUsername(username);
		user.setJwt("");
		userAuthRepository.save(user);
		
		logger.info("logging out  {}", user);
		
		// clear authentication object from security context.
		SecurityContextHolder.getContext().setAuthentication(null);
		
	}

}
