package com.practise.spring.security.controllers;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.practise.spring.security.models.CustomAuthUserDetails;
import com.practise.spring.security.models.RegistrationRequest;
import com.practise.spring.security.services.UserAuthenticationService;

@RestController
@RequestMapping(value="/api")
public class UserAuthenticationController {

	
	@Autowired
	private UserAuthenticationService userAuthenticationService;
	
	private Logger logger = LoggerFactory.getLogger(UserAuthenticationController.class);
	
	
	@GetMapping(value = "/test") 
	public ResponseEntity<?> test() {
		
		logger.info("test API is reachable");
		return ResponseEntity.ok().body("{ \"status\" : \"test successful\"}");
	}

	
	@GetMapping(value = "/login") 
	public ResponseEntity<?> login() {
		
		return ResponseEntity.ok().body("{ \"status\" : \"Authentication successful\"}");
	}
	
	@PostMapping(value = "/register") 
	public ResponseEntity<?> registerUser(
			@RequestBody RegistrationRequest registrationRequest,
			HttpServletRequest request, 
			HttpServletResponse response) {
		
		userAuthenticationService.registerUser(registrationRequest);
		return ResponseEntity
				.created(URI.create("http://localhost:8000/api/login"))
				.body("{ \"status\" : \"Registration successful\"}");
	}
	
	@GetMapping(value = "/get-users", name = "This is a resource API") 
	public ResponseEntity<?> getAllRegisteredUsersEmail() {
		
		List<CustomAuthUserDetails> users = userAuthenticationService.findAllUsers();
		return ResponseEntity.ok().body(users);
	}
	


}
