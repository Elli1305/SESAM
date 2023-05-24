package com.gpse.sesam.web.filter;

import com.gpse.sesam.configuration.SecurityConstants;
import com.gpse.sesam.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
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

	private final JwtUtils jwtUtils;

	public JWTAuthorizationFilter(final AuthenticationManager authenticationManager,
								  final UserDetailsService userDetailsService,
								  final SecurityConstants securityConstants) {
		super(authenticationManager);
		this.userDetailsService = userDetailsService;
		this.securityConstants = securityConstants;
		jwtUtils = new JwtUtils(securityConstants);
	}


	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
									final FilterChain filterChain) throws IOException, ServletException {
		final UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
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

	private UsernamePasswordAuthenticationToken getAuthentication(final HttpServletRequest request) {
		final String token = request.getHeader(securityConstants.getTokenHeader());
		if (tokenIsValid(token)) {
			try {
				final Jws<Claims> parsedToken = jwtUtils.getClaimsJws(token);

				final String username = parsedToken.getBody().getSubject();

				final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				if (username != null && !username.equals("")) {
					return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				}
			} catch (final ExpiredJwtException exception) {
				LOG.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
			} catch (final UnsupportedJwtException exception) {
				LOG.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
			} catch (final MalformedJwtException exception) {
				LOG.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
			} catch (final SecurityException exception) {
				LOG.warn("Request to parse JWT with invalid signature : {} failed : {}", token,
						exception.getMessage());
			} catch (final IllegalArgumentException exception) {
				LOG.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
			}
		}

		return null;
	}

	private boolean tokenIsValid(final String token) {
		return token != null && !token.equals("") && token.startsWith(securityConstants.getTokenPrefix());
	}


}