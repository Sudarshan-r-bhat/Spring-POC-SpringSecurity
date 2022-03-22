package com.practise.spring.security.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.practise.spring.security.models.CustomAuthUserDetails;
import com.practise.spring.security.models.RegistrationRequest;
import com.practise.spring.security.repositories.ICustomUserDetailsRepository;

@Service
public class UserAuthenticationService {
	
	@Autowired
	private ICustomUserDetailsRepository userDetailsRepository;
	
	@Autowired
	private UserDetailsManager userDetailsManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	public void registerUser(RegistrationRequest registrationRequest) {
		
		List<GrantedAuthority> authorities= new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("READ"));
		authorities.add(new SimpleGrantedAuthority("WRITE"));
		
		CustomAuthUserDetails userDetails = new CustomAuthUserDetails(registrationRequest.getEmail(),
				passwordEncoder.encode(registrationRequest.getPassword()), 
				authorities, 
				true, true, true, true);
		
		userDetailsManager.createUser(userDetails);
	}
	
	public void updateUserPassword(RegistrationRequest registrationRequest) {
		
		CustomAuthUserDetails user = (CustomAuthUserDetails) userDetailsManager.loadUserByUsername(registrationRequest.getEmail());
		
		String givenOldPassword = passwordEncoder.encode(registrationRequest.getPassword());
		String oldPassword = user.getPassword();
		String newPassword = registrationRequest.getMatchingPassword();
		
		if(oldPassword.equals(givenOldPassword)) {
			user.setPassword(newPassword);
		}
		userDetailsManager.updateUser(user);
	}

	public List<CustomAuthUserDetails> findAllUsers() {
		
		return userDetailsRepository.findAll();
	}
	
}
