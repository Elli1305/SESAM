package com.gpse.sesam.web;

import com.gpse.sesam.SecurityConstants;
import com.gpse.sesam.domain.SesamUser;
import com.gpse.sesam.domain.SesamUserService;
import com.gpse.sesam.web.cmd.Credentials;
import com.gpse.sesam.web.cmd.PasswordResetCmd;
import com.gpse.sesam.web.cmd.SesamUserCmd;
import com.gpse.sesam.web.cmd.UpdatePasswordCmd;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class SesamUserController {
	private final SesamUserService service;
	private final AuthenticationManager authenticationManager;
	private final SecurityConstants securityConstants;

	@Autowired
	public SesamUserController(final SesamUserService service, AuthenticationManager authenticationManager,
							   final SecurityConstants securityConstants) {
		this.service = service;
		this.authenticationManager = authenticationManager;
		this.securityConstants = securityConstants;
	}

	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public SesamUser createUser(@RequestBody SesamUserCmd sesamUserCmd) {
		return service.createUser(sesamUserCmd);
	}

	@PostMapping("/authenticate")
	@ResponseStatus(HttpStatus.OK)
	public LoginResponse authenticate(@RequestBody Credentials credentials) {
		Authentication authentication = authenticationManager
				.authenticate(
						new UsernamePasswordAuthenticationToken(credentials.geteMail(), credentials.getPassword())
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);

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
		return new LoginResponse(token, user);
	}

	@PostMapping("/password_reset")
	@ResponseStatus(HttpStatus.CREATED)
	public void passwordReset(@RequestBody final PasswordResetCmd passwordResetCmd) {
		SesamUser user = (SesamUser) service.loadUserByUsername(passwordResetCmd.getEmail());
		String token = UUID.randomUUID().toString();

		service.createPasswordResetToken(user, token);
	}

	@PostMapping("/update_password")
	@ResponseStatus(HttpStatus.OK)
	public void updatePasswordWithToken(@RequestBody final UpdatePasswordCmd updatePasswordCmd) {
		service.updatePasswordWithToken(
				updatePasswordCmd.getToken(),
				updatePasswordCmd.getPassword()
		);
	}
}
