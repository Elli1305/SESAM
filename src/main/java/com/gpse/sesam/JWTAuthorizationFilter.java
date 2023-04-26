package com.gpse.sesam;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JWTAuthorizationFilter.class);
	private final UserDetailsService userDetailsService;
	private final SecurityConstants securityConstants;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager,
								  final UserDetailsService userDetailsService,
								  final SecurityConstants securityConstants) {
		super(authenticationManager);
		this.userDetailsService = userDetailsService;
		this.securityConstants = securityConstants;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws IOException, ServletException {
		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		if (authentication == null) {
			filterChain.doFilter(request, response);
			return;
		}

		authentication.setDetails(
				new WebAuthenticationDetailsSource().buildDetails(request)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(securityConstants.getTokenHeader());
		if (token != null && !token.equals("") && token.startsWith(securityConstants.getTokenPrefix())) {
			try {
				byte[] signingKey = securityConstants.getJwtSecret().getBytes();

				Jws<Claims> parsedToken = Jwts.parserBuilder()
						.setSigningKey(signingKey).build()
						.parseClaimsJws(token.replace(securityConstants.getTokenPrefix(), "").strip());

				String username = parsedToken.getBody().getSubject();

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				if (username != null && !username.equals("")) {
					return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				}
			} catch (ExpiredJwtException exception) {
				LOG.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
			} catch (UnsupportedJwtException exception) {
				LOG.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
			} catch (MalformedJwtException exception) {
				LOG.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
			} catch (SecurityException exception) {
				LOG.warn("Request to parse JWT with invalid signature : {} failed : {}", token,
						exception.getMessage());
			} catch (IllegalArgumentException exception) {
				LOG.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
			}
		}

		return null;
	}
}