package com.practise.spring.security.models;

public class RegistrationRequest {
	private String email;
	private String password;
	private String matchingPassword;
	private String mobile;
	private Address address;
	
	static class Address {
		private String country;
		private String state;
		private String street;
		private String zipCode;
		
		@Override
		public String toString() {
			return "Address [country=" + country + ", state=" + state + ", street=" + street + ", zipCode=" + zipCode
					+ "]";
		}
	}

	@Override
	public String toString() {
		return "RegistrationRequest [email=" + email + ", password=" + password + ", matchingPassword="
				+ matchingPassword + ", mobile=" + mobile + ", address=" + address + "]";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	
	
	
	
}
