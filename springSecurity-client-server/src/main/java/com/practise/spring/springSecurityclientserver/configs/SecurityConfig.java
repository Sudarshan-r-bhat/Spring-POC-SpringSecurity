package com.practise.spring.springSecurityclientserver.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

@EnableWebSecurity(debug = true)
public class SecurityConfig {
	
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.authorizeRequests(customizer -> 
			customizer.anyRequest().authenticated())
				.oauth2Login(loginCustomizer -> loginCustomizer.loginPage("/oauth2/authorization/articles-client-oidc")) 
				.oauth2Client(withDefaults());
		return http.build();
	}
	

}
