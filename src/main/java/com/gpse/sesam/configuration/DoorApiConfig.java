package com.gpse.sesam.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("sesam.door.api")
public class DoorApiConfig {

	private String url;

	private String username;

	private String password;

	public void setUrl(final String url) {
		this.url = url;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
