package com.gpse.sesam.configuration;

import com.gpse.sesam.web.filter.JWTAuthenticationFilter;
import com.gpse.sesam.web.filter.JWTAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
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
	private static final Logger LOG = LoggerFactory.getLogger(SecurityConfiguration.class);
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
	public SecurityFilterChain filterChain(final HttpSecurity http) {

		try {
			http.csrf().disable().cors()
					.and()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and()
					.authorizeHttpRequests()
					.requestMatchers(HttpMethod.GET).permitAll()
					.requestMatchers("/api/**").permitAll()
					.and()
					.addFilter(new JWTAuthorizationFilter(authenticationManager(), userDetailsService,
							securityConstants))
					.addFilter(new JWTAuthenticationFilter(authenticationManager(), securityConstants));

			http.headers().frameOptions().disable();

			return http.build();
		} catch (Exception e) { //NOPMD
			throw new BeanCreationException("could not create security filter chain bean", e);
		}
	}


	@Bean
	@Profile({"test", "presentation"})
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers(
				new AntPathRequestMatcher("/h2-console/**")
		);
	}


	@Bean
	public AuthenticationManager authenticationManager() {
		try {
			return authConfig.getAuthenticationManager();
		} catch (Exception e) { //NOPMD
			throw new BeanCreationException("could not create authentication manger bean", e);
		}
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
