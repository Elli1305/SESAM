package com.gpse.sesam.util;

import com.gpse.sesam.configuration.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class JwtUtils {
	private final SecurityConstants securityConstants;

	public JwtUtils(final SecurityConstants securityConstants) {
		this.securityConstants = securityConstants;
	}

	public Jws<Claims> getClaimsJws(final String token) {
		final byte[] signingKey = securityConstants.getJwtSecret().getBytes();

		return Jwts.parserBuilder()
				.setSigningKey(signingKey).build()
				.parseClaimsJws(token.replace(securityConstants.getTokenPrefix(), "").strip());
	}
}
