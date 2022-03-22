package com.practise.spring.security.models;

import java.util.Collection;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class CustomAuthUserDetails implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String username; 
	private String password; 
	
	@Lob
	private String jwt;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<GrantedAuthority> authorities;
	
	private boolean isAccountNonExpired;
	private boolean isAccountNonBlocked;
	private boolean isCredentialsNonExpired; 
	private boolean isEnabled;
	
	// extra
	private String mobile;
	
	public CustomAuthUserDetails() {}
	
	public CustomAuthUserDetails(String username, String password, List<GrantedAuthority> authorities,
		boolean isAccountNonExpired, boolean isAccountNonBlocked, 
		boolean isCredentialsNonExpired, boolean isEnabled) {
		
		this.authorities = authorities;
		this.username = username;
		this.password = password;
		this.isAccountNonBlocked = isAccountNonBlocked;
		this.isAccountNonExpired = isAccountNonExpired;
		this.isEnabled = isEnabled;
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.isAccountNonBlocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.isEnabled;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isAccountNonBlocked() {
		return isAccountNonBlocked;
	}

	public void setAccountNonBlocked(boolean isAccountNonBlocked) {
		this.isAccountNonBlocked = isAccountNonBlocked;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public void setAccountNonExpired(boolean isAccountNonExpired) {
		this.isAccountNonExpired = isAccountNonExpired;
	}

	public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

}
