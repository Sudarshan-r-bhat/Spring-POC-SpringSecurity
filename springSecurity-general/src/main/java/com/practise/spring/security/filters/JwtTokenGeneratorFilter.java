package com.practise.spring.security.filters;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jose.util.StandardCharset;
import com.practise.spring.security.models.AppConstants;
import com.practise.spring.security.models.CustomAuthUserDetails;
import com.practise.spring.security.repositories.ICustomUserDetailsRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtTokenGeneratorFilter extends OncePerRequestFilter {
	
	@Autowired
	private ICustomUserDetailsRepository userAuthRepository;
	
	private final Logger logger = LoggerFactory.getLogger(JwtTokenGeneratorFilter.class);
	
	private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
		Set<String> authoritiesSet = new HashSet<>();
		for(GrantedAuthority authority: collection) {
			authoritiesSet.add(authority.getAuthority());
		}
		return String.join(",", authoritiesSet);
	}
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(authentication != null) {
			
			logger.info("JwtTokenGeneratorFilter called for {}", authentication.getPrincipal());

			String jwtString = request.getHeader("jwt");
			if(jwtString == null) { // || jwt is expired.
				SecretKey secretKey = Keys.hmacShaKeyFor( AppConstants.JWT.SECRET_KEY.getBytes(StandardCharset.UTF_8) );
				jwtString = Jwts.builder().setIssuer("Spring InmemoryDb Service").setSubject("basic authorization details")
						.claim("username", authentication.getName())
						.claim("authorities" , populateAuthorities(authentication.getAuthorities()))
						.setIssuedAt(new Date())
						.setExpiration(new Date( new Date().getTime() + 30_000_000 ))
						.signWith(secretKey).compact();
				
				CustomAuthUserDetails user = (CustomAuthUserDetails) userAuthRepository.findByUsername(authentication.getName());
				user.setJwt(jwtString);
				userAuthRepository.save(user);
				
				logger.info("new Jwt String generated = {} \nSaved to Db.", jwtString);
				
				response.addCookie(new Cookie("jwt", jwtString));
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getServletPath().equals("/api/register") 
				|| request.getServletPath().contains("/h2-console")
				|| request.getServletPath().contains("/authz/oauth2login");
	}
}
