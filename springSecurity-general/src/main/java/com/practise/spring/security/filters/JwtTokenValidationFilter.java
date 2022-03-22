package com.practise.spring.security.filters;

import java.io.IOException;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jose.util.StandardCharset;
import com.practise.spring.security.models.AppConstants;
import com.practise.spring.security.models.CustomAuthUserDetails;
import com.practise.spring.security.repositories.ICustomUserDetailsRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtTokenValidationFilter extends OncePerRequestFilter {

	@Autowired
	private ICustomUserDetailsRepository userAuthRepository;
	
	private final Logger logger = LoggerFactory.getLogger(JwtTokenValidationFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwt = request.getHeader("jwt");
		
		if(jwt != null) {
			try {
				SecretKey key = Keys.hmacShaKeyFor(AppConstants.JWT.SECRET_KEY.getBytes(StandardCharset.UTF_8));
				Claims claims = Jwts
						.parserBuilder()
						.setSigningKey(key)
						.build()
						.parseClaimsJws(jwt)
						.getBody();
				String username = String.valueOf(claims.get("username"));
				String authorities = (String) claims.get("authorities");
				
				CustomAuthUserDetails user = userAuthRepository.findByUsername(username);
				
				if(!user.getJwt().equals(jwt))
					throw new BadCredentialsException("Invalid Token received!");
				
				Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
						AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
			
				SecurityContextHolder.getContext().setAuthentication(auth);
			
			} catch(Exception e) {
				throw new BadCredentialsException("Invalid Token received!");
			}
		}
		filterChain.doFilter(request, response);		
	}

}
