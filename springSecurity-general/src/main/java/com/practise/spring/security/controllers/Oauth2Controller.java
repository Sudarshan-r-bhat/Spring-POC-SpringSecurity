package com.practise.spring.security.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping(value="/authz")
public class Oauth2Controller {
	
	private Logger logger = LoggerFactory.getLogger(Oauth2Controller.class);
	
	@GetMapping(value = "/oauth2login") 
	public RedirectView oauth2login(OAuth2AuthenticationToken token) {
		
		logger.info("Entered oauth2 protected resource.{} ", token == null ? null : token.getPrincipal());
		
		return new RedirectView("https://sudarshan-bhat.blogspot.com/");
	}
}
