package com.gpse.sesam.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gpse.sesam.configuration.SecurityConstants;
import com.gpse.sesam.web.response.LoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final SecurityConstants securityConstants;

	public JWTAuthenticationFilter(final AuthenticationManager authenticationManager,
								   final SecurityConstants securityConstants) {
		super(authenticationManager);
		this.securityConstants = securityConstants;
		setPasswordParameter("password");
		setUsernameParameter("eMail");
		setFilterProcessesUrl(securityConstants.getAuthLoginUrl());
	}

	@Override
	protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
											final FilterChain chain, final Authentication authentication)
			throws IOException {
		final UserDetails user = (UserDetails) authentication.getPrincipal();

		final List<String> roles = user.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.toList();

		final byte[] signingKey = securityConstants.getJwtSecret().getBytes();

		final String token = Jwts.builder()
				.signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
				.setHeaderParam("typ", securityConstants.getTokenType())
				.setIssuer(securityConstants.getTokenIssuer())
				.setAudience(securityConstants.getTokenAudience())
				.setSubject(user.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + securityConstants.getTokenExpiration()))
				.claim("rol", roles)
				.compact();

		response.addHeader(securityConstants.getTokenHeader(), securityConstants.getTokenPrefix() + " " + token);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		response.getWriter().write(ow.writeValueAsString(new LoginResponse(token, user)));
	}
}
