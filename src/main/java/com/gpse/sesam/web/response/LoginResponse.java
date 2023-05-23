package com.gpse.sesam.web.response;

import org.springframework.security.core.userdetails.UserDetails;

public class LoginResponse {
	private String token;
	private UserDetails user;

	public LoginResponse(final String token, final UserDetails user) {
		this.token = token;
		this.user = user;
	}

	public LoginResponse() {
	}

	public String getToken() {
		return token;
	}

	public void setToken(final String token) {
		this.token = token;
	}

	public UserDetails getUser() {
		return user;
	}

	public void setUser(final UserDetails user) {
		this.user = user;
	}
}
