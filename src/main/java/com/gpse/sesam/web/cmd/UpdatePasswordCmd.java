package com.gpse.sesam.web.cmd;

public class UpdatePasswordCmd {
	private String token;

	private String password;

	public String getToken() {
		return token;
	}

	public void setToken(final String token) {
		this.token = token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}
}
