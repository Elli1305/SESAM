package com.gpse.sesam;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("security") //<1>
public final class SecurityConstants {
	private String jwtSecret;
	private String tokenHeader;
	private String tokenPrefix;
	private String tokenType;
	private String tokenIssuer;
	private String tokenAudience;

	private long tokenExpiration;

	public String getJwtSecret() {
		return jwtSecret;
	}

	public void setJwtSecret(String jwtSecret) {
		this.jwtSecret = jwtSecret;
	}

	public String getTokenHeader() {
		return tokenHeader;
	}

	public void setTokenHeader(String tokenHeader) {
		this.tokenHeader = tokenHeader;
	}

	public String getTokenPrefix() {
		return tokenPrefix;
	}

	public void setTokenPrefix(String tokenPrefix) {
		this.tokenPrefix = tokenPrefix;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getTokenIssuer() {
		return tokenIssuer;
	}

	public void setTokenIssuer(String tokenIssuer) {
		this.tokenIssuer = tokenIssuer;
	}

	public String getTokenAudience() {
		return tokenAudience;
	}

	public void setTokenAudience(String tokenAudience) {
		this.tokenAudience = tokenAudience;
	}

	public long getTokenExpiration() {
		return tokenExpiration;
	}

	public void setTokenExpiration(long tokenExpiration) {
		this.tokenExpiration = tokenExpiration;
	}
}