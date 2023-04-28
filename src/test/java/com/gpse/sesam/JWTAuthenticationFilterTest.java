package com.gpse.sesam;

import com.gpse.sesam.domain.SesamUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class JWTAuthenticationFilterTest {

	private JWTAuthenticationFilter authenticationFilter;

	private SecurityConstants securityConstants;

	@BeforeEach
	void setUp() {
		securityConstants = new SecurityConstants();
		securityConstants.setTokenHeader("Authorization");
		securityConstants.setTokenPrefix("Bearer");
		securityConstants.setJwtSecret("Xn2r5u8x!A%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C*F)J@NcR");
		securityConstants.setTokenExpiration(864_000_000);
		securityConstants.setTokenAudience("");
		securityConstants.setTokenType("");

		authenticationFilter = new JWTAuthenticationFilter(mock(AuthenticationManager.class), securityConstants);
	}


	@Test
	void parameterNamesShouldBeSetCorrectly() {
		assertThat(authenticationFilter.getPasswordParameter(), is("password"));
		assertThat(authenticationFilter.getUsernameParameter(), is("eMail"));
	}

	@Test
	void successfulAuthenticationShouldReturnValidJWT() throws IOException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		MockHttpServletResponse res = new MockHttpServletResponse();
		Authentication authentication = mock(Authentication.class);
		FilterChain filterChain = mock(FilterChain.class);

		SesamUser user = new SesamUser("test@test.com", "password", "Test", "User", Collections.emptyList());


		when(authentication.getPrincipal()).thenReturn(user);

		authenticationFilter.successfulAuthentication(req, res, filterChain, authentication);

		String token = res.getHeader(securityConstants.getTokenHeader());

		assertThat(token, notNullValue());

		Jws<Claims> claims = parseToken(token);

		assertThat(claims.getBody().getSubject(), is(user.getUsername()));
		assertThat(claims.getBody().get("rol"), is(Collections.emptyList()));
		assertThat(new String(res.getContentAsByteArray()), not(emptyString()));
	}

	private Jws<Claims> parseToken(String token) {
		byte[] signingKey = securityConstants.getJwtSecret().getBytes();

		return Jwts.parserBuilder()
				.setSigningKey(signingKey).build()
				.parseClaimsJws(token.replace(securityConstants.getTokenPrefix(), "").strip());
	}

}
