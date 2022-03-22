package com.practise.spring.security.configs;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.practise.spring.security.filters.CustomLogoutHandler;
import com.practise.spring.security.filters.JwtTokenGeneratorFilter;
import com.practise.spring.security.filters.JwtTokenValidationFilter;
import com.practise.spring.security.models.AppConstants;

@EnableWebSecurity(debug=true)
public class SecurityConfigs  {

	
	@Configuration
	@Order(1)
	public static class httBasicSecurityConfigs extends WebSecurityConfigurerAdapter {
		
		@Autowired 
		private CustomLogoutHandler logoutHandler;
		
		@Autowired
		private JwtTokenValidationFilter jwtTokenValidationFilter;
		
		@Autowired
		private JwtTokenGeneratorFilter jwtTokenGeneratorFilter;
		
		@Autowired
		private CustomUserDetailsManager customUserDetailsManager;

		
		@Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
			.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) )
	        .cors()
	        .configurationSource(new CorsConfigurationSource() {
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowedOrigins(Collections.singletonList("*"));
					config.setAllowedMethods(Collections.singletonList("*"));
					config.setAllowCredentials(true);
					config.setAllowedHeaders(Collections.singletonList("*"));
					config.setExposedHeaders(Arrays.asList("Authorization", "jwt"));  // This is allow headers to be sent in response from this server to other servers requesting service.
					config.setMaxAge(3600L); // 5mins
					return config;
				}})
	        .and()
	        .csrf().disable()
	        .addFilterBefore(jwtTokenValidationFilter, LogoutFilter.class)
	        .addFilterAfter( jwtTokenGeneratorFilter, BasicAuthenticationFilter.class)
	        .httpBasic().and()
	        .authorizeRequests()
	                .antMatchers("/h2-console/**").permitAll()
	                .antMatchers("/api/register").permitAll()
	                .antMatchers("/api/get-users").authenticated()
	                .mvcMatchers("/api/login").hasAuthority("READ")
	                .and()
	                .logout()
	                .logoutSuccessUrl("https://www.google.co.in/")
	                .deleteCookies("jwt")
	                .addLogoutHandler(logoutHandler)
	                ;
		}


		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(customUserDetailsManager);
		}

	    @Bean 
	    public PasswordEncoder passwordEncoder() {
	    	return new BCryptPasswordEncoder();
	    }
	}
	
	@Configuration
	public static class OAuth2SecurityConfigs extends WebSecurityConfigurerAdapter {
		
		@Autowired 
		private CustomLogoutHandler logoutHandler;
		
		@Autowired
		private JwtTokenValidationFilter jwtTokenValidationFilter;
		
		@Autowired
		private JwtTokenGeneratorFilter jwtTokenGeneratorFilter;
		
		@Autowired
		private CustomUserDetailsManager customUserDetailsManager;

		@Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
			.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) )
	        .cors()
	        .configurationSource(new CorsConfigurationSource() {
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowedOrigins(Collections.singletonList("*"));
					config.setAllowedMethods(Collections.singletonList("*"));
					config.setAllowCredentials(true);
					config.setAllowedHeaders(Collections.singletonList("*"));
					config.setExposedHeaders(Arrays.asList("Authorization", "jwt"));  // This is allow headers to be sent in response from this server to other servers requesting service.
					config.setMaxAge(3600L); // 5mins
					return config;
				}})
			.and()
			.csrf().disable()
			.addFilterBefore(jwtTokenValidationFilter, LogoutFilter.class)
			.addFilterAfter( jwtTokenGeneratorFilter, BasicAuthenticationFilter.class)
			.authorizeRequests()
		            .antMatchers("/authz/**").authenticated().and()
		            .oauth2Login()
		            .clientRegistrationRepository(clientRegistrationRepository())
		            .and()
					.logout()
					.logoutSuccessUrl("https://www.google.co.in/")
					.deleteCookies("jwt")
					.addLogoutHandler(logoutHandler)
					;
		}
	   
		public ClientRegistration clientRegistration() {
		    	
	    	ClientRegistration clientRegistration = CommonOAuth2Provider.GOOGLE.getBuilder("google")
	    	.clientId(AppConstants.OAUTH2.GOOGLE.GOOGLE_CLIENT_ID)
	    	.clientSecret(AppConstants.OAUTH2.GOOGLE.GOOGLE_CLIENT_SECRET)
	    	.build();
	    	return clientRegistration;
	    }
	    public ClientRegistrationRepository clientRegistrationRepository() {
	    	ClientRegistrationRepository clientRegistrationRepository = new InMemoryClientRegistrationRepository(clientRegistration());
			return clientRegistrationRepository;
	    	
	    }
	    
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(customUserDetailsManager);
		}

	}
}
