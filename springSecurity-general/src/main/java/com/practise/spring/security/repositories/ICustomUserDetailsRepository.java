package com.practise.spring.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.practise.spring.security.models.CustomAuthUserDetails;

@Repository
public interface ICustomUserDetailsRepository extends JpaRepository<CustomAuthUserDetails, Long> {

	public CustomAuthUserDetails findByUsername(String username);

	public boolean existsByUsername(String username);

	public CustomAuthUserDetails findByJwt(String jwt);

}
