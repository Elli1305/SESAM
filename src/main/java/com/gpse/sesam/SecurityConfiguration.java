package com.gpse.sesam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	private final AuthenticationConfiguration authConfig;
	private final UserDetailsService userDetailsService;
	private final SecurityConstants securityConstants;

	@Autowired
	public SecurityConfiguration(final AuthenticationConfiguration authConfig,
								 final UserDetailsService userDetailsService,
								 final SecurityConstants securityConstants) {
		this.authConfig = authConfig;
		this.userDetailsService = userDetailsService;
		this.securityConstants = securityConstants;
	}


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf().disable().cors()
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeHttpRequests()
				.requestMatchers(HttpMethod.GET).permitAll()
				.requestMatchers("/api/**").permitAll()
				.and()
				.addFilter(new JWTAuthorizationFilter(authenticationManager(), userDetailsService, securityConstants))
				.addFilter(new JWTAuthenticationFilter(authenticationManager(), securityConstants));

		http.headers().frameOptions().disable();

		return http.build();
	}


	@Bean
	@Profile("test")
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers(
				new AntPathRequestMatcher("/h2-console/**")
		);
	}

	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() { //<8>
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
		corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "PATCH", "DELETE"));
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}
}
