package com.gpse.sesam;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


class JWTAuthorizationFilterTest {

	private JWTAuthorizationFilter jwtAuthenticationFilter;
	private final UserDetailsService userDetailsService = mock(UserDetailsService.class);

	private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
	private SecurityConstants securityConstants;
	private ListAppender<ILoggingEvent> listAppender;

	private final Logger logger = (Logger) LoggerFactory.getLogger(JWTAuthorizationFilter.class);

	@BeforeEach
	void setUp() {
		listAppender = setUpLogger();
		securityConstants = new SecurityConstants();
		securityConstants.setTokenHeader("Authorization");
		securityConstants.setTokenPrefix("Bearer");
		securityConstants.setJwtSecret("Xn2r5u8x!A%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C*F)J@NcR");
		securityConstants.setTokenExpiration(864_000_000);
		securityConstants.setTokenAudience("");
		securityConstants.setTokenType("");
		jwtAuthenticationFilter = new JWTAuthorizationFilter(authenticationManager, userDetailsService,
				securityConstants);
	}

	@AfterEach
	void tearDown() {
		logger.detachAppender(listAppender);
	}

	@Test
	void shouldSetAuthenticationContextIfTokenIsValid() throws ServletException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.addHeader(securityConstants.getTokenHeader(), generateToken());
		MockHttpServletResponse res = new MockHttpServletResponse();
		FilterChain filterChain = mock(FilterChain.class);

		when(userDetailsService.loadUserByUsername("TestUser")).thenReturn(mock(UserDetails.class));

		jwtAuthenticationFilter.doFilterInternal(req, res, filterChain);

		verify(filterChain).doFilter(req, res);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		assertThat(authentication, notNullValue());
		assertThat(authentication.getDetails(), instanceOf(WebAuthenticationDetails.class));
	}

	@ParameterizedTest
	@ValueSource(strings = {"", " ", "validTokenWithoutPrefix"})
	void ifTokenIsNotSetCorrectlyContinueWithFilterChain(String token) throws ServletException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.addHeader(securityConstants.getTokenHeader(), token);
		MockHttpServletResponse res = new MockHttpServletResponse();
		FilterChain filterChain = mock(FilterChain.class);

		jwtAuthenticationFilter.doFilterInternal(req, res, filterChain);

		verifyNoInteractions(userDetailsService);
		verifyNoInteractions(authenticationManager);
		verify(filterChain).doFilter(req, res);
	}

	@Test
	void shouldRaiseExceptionIfJwtIsMalformated() throws ServletException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.addHeader(securityConstants.getTokenHeader(), "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9");
		MockHttpServletResponse res = new MockHttpServletResponse();
		FilterChain filterChain = mock(FilterChain.class);


		jwtAuthenticationFilter.doFilterInternal(req, res, filterChain);

		List<ILoggingEvent> logsList = listAppender.list;

		assertThat(logsList.size(), is(1));
		assertThat(logsList.get(0).getLevel(), is(Level.WARN));
		assertThat(logsList.get(0).getMessage(), containsString("Request to parse invalid JWT"));

		verifyNoInteractions(userDetailsService);
		verifyNoInteractions(authenticationManager);
		verify(filterChain).doFilter(req, res);
	}

	@Test
	void shouldRaiseExceptionIfSecretIsInvalid() throws ServletException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		String token = generateToken();
		securityConstants.setJwtSecret("Xn2r5u8x!A%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C*F)J!NcR");
		req.addHeader(securityConstants.getTokenHeader(), token);
		MockHttpServletResponse res = new MockHttpServletResponse();
		FilterChain filterChain = mock(FilterChain.class);


		jwtAuthenticationFilter.doFilterInternal(req, res, filterChain);

		List<ILoggingEvent> logsList = listAppender.list;

		assertThat(logsList.size(), is(1));
		assertThat(logsList.get(0).getLevel(), is(Level.WARN));
		assertThat(logsList.get(0).getMessage(), containsString("Request to parse JWT with invalid signature"));

		verifyNoInteractions(userDetailsService);
		verifyNoInteractions(authenticationManager);
		verify(filterChain).doFilter(req, res);
	}

	@Test
	void shouldRaiseExceptionIfTokenIsExpired() throws ServletException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		securityConstants.setTokenExpiration(0L);
		req.addHeader(securityConstants.getTokenHeader(), generateToken());
		MockHttpServletResponse res = new MockHttpServletResponse();
		FilterChain filterChain = mock(FilterChain.class);


		jwtAuthenticationFilter.doFilterInternal(req, res, filterChain);

		List<ILoggingEvent> logsList = listAppender.list;

		assertThat(logsList.size(), is(1));
		assertThat(logsList.get(0).getLevel(), is(Level.WARN));
		assertThat(logsList.get(0).getMessage(), containsString("Request to parse expired JWT"));

		verifyNoInteractions(userDetailsService);
		verifyNoInteractions(authenticationManager);
		verify(filterChain).doFilter(req, res);
	}

	@Test
	void shouldRaiseExceptionIfTokenIsNotSet() throws ServletException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		securityConstants.setTokenExpiration(0L);
		req.addHeader(securityConstants.getTokenHeader(), securityConstants.getTokenPrefix());
		MockHttpServletResponse res = new MockHttpServletResponse();
		FilterChain filterChain = mock(FilterChain.class);


		jwtAuthenticationFilter.doFilterInternal(req, res, filterChain);

		List<ILoggingEvent> logsList = listAppender.list;

		assertThat(logsList.size(), is(1));
		assertThat(logsList.get(0).getLevel(), is(Level.WARN));
		assertThat(logsList.get(0).getMessage(), containsString("Request to parse empty or null JWT"));

		verifyNoInteractions(userDetailsService);
		verifyNoInteractions(authenticationManager);
		verify(filterChain).doFilter(req, res);
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {""})
	void shouldContinueWithFilterChainIfUsernameIsEmpty(String username) throws ServletException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.addHeader(securityConstants.getTokenHeader(), generateToken(username));
		MockHttpServletResponse res = new MockHttpServletResponse();
		FilterChain filterChain = mock(FilterChain.class);


		jwtAuthenticationFilter.doFilterInternal(req, res, filterChain);

		verify(userDetailsService).loadUserByUsername(null);
		verifyNoInteractions(authenticationManager);
		verify(filterChain).doFilter(req, res);
	}


	@Test
	void shouldRaiseExceptionIfTokenIsNotSigned() throws ServletException, IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		String token = generateTokenWithoutSignature();
		req.addHeader(securityConstants.getTokenHeader(), token);
		MockHttpServletResponse res = new MockHttpServletResponse();
		FilterChain filterChain = mock(FilterChain.class);


		jwtAuthenticationFilter.doFilterInternal(req, res, filterChain);

		List<ILoggingEvent> logsList = listAppender.list;

		assertThat(logsList.size(), is(1));
		assertThat(logsList.get(0).getLevel(), is(Level.WARN));
		assertThat(logsList.get(0).getMessage(), containsString("Request to parse unsupported JWT"));

		verifyNoInteractions(userDetailsService);
		verifyNoInteractions(authenticationManager);
		verify(filterChain).doFilter(req, res);
	}

	private ListAppender<ILoggingEvent> setUpLogger() {
		listAppender = new ListAppender<>();
		listAppender.start();
		logger.addAppender(listAppender);
		return listAppender;
	}

	private String generateToken(String username) {
		return "Bearer " + Jwts.builder()
				.signWith(Keys.hmacShaKeyFor(securityConstants.getJwtSecret().getBytes()), SignatureAlgorithm.HS512)
				.setHeaderParam("typ", securityConstants.getTokenType())
				.setIssuer(securityConstants.getTokenIssuer())
				.setAudience(securityConstants.getTokenAudience())
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + securityConstants.getTokenExpiration()))
				.claim("rol", Collections.EMPTY_LIST)
				.compact();
	}

	private String generateTokenWithoutSignature() {
		return "Bearer " + Jwts.builder()
				.setHeaderParam("typ", securityConstants.getTokenType())
				.setIssuer(securityConstants.getTokenIssuer())
				.setAudience(securityConstants.getTokenAudience())
				.setSubject("TestUser")
				.setExpiration(new Date(System.currentTimeMillis() + securityConstants.getTokenExpiration()))
				.claim("rol", Collections.EMPTY_LIST)
				.compact();
	}

	private String generateToken() {
		return generateToken("TestUser");
	}


}