package com.practise.spring.security.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import com.practise.spring.security.models.CustomAuthUserDetails;
import com.practise.spring.security.repositories.ICustomUserDetailsRepository;

@Configuration
public class CustomUserDetailsManager implements UserDetailsManager {

	@Autowired
	private ICustomUserDetailsRepository userDetailsRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return userDetailsRepository.findByUsername(username);
	}

	@Override
	public void createUser(UserDetails user) {
		
		CustomAuthUserDetails customUser = (CustomAuthUserDetails) user;
		CustomAuthUserDetails result = userDetailsRepository.save(customUser);
		if(result == null )
			throw new IllegalArgumentException("Wrong user object passed for registration");
	}

	@Override
	public void updateUser(UserDetails user) {
		
		CustomAuthUserDetails customUser = (CustomAuthUserDetails) user;
		CustomAuthUserDetails result = userDetailsRepository.save(customUser);
		if(result == null )
			throw new IllegalArgumentException("Wrong user object passed for userUpdate");
		
	}

	@Override
	public void deleteUser(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean userExists(String username) {
		return userDetailsRepository.existsByUsername(username);
	}

}
