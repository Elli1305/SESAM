package com.gpse.sesam.web;

import org.springframework.security.core.userdetails.UserDetails;

public class LoginResponse {
	private String token;
	private UserDetails user;

	public LoginResponse(String token, UserDetails user) {
		this.token = token;
		this.user = user;
	}

	public LoginResponse() {
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserDetails getUser() {
		return user;
	}

	public void setUser(UserDetails user) {
		this.user = user;
	}
}
