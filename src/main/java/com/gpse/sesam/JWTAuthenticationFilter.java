package com.gpse.sesam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gpse.sesam.web.LoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
	private final SecurityConstants securityConstants;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, SecurityConstants securityConstants) {
		super(authenticationManager);
		this.securityConstants = securityConstants;
		setPasswordParameter("password");
		setUsernameParameter("eMail");
		setFilterProcessesUrl(securityConstants.getAuthLoginUrl());
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
											FilterChain chain, Authentication authentication) throws IOException {
		UserDetails user = (UserDetails) authentication.getPrincipal();

		List<String> roles = user.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.toList();

		byte[] signingKey = securityConstants.getJwtSecret().getBytes();

		String token = Jwts.builder()
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
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		response.getWriter().write(ow.writeValueAsString(new LoginResponse(token, user)));
	}
}
