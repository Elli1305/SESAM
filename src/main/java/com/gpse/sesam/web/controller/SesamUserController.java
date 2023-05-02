package com.gpse.sesam.web.controller;

import com.gpse.sesam.configuration.SecurityConstants;
import com.gpse.sesam.domain.user.SesamUser;
import com.gpse.sesam.domain.user.SesamUserService;
import com.gpse.sesam.web.cmd.PasswordResetCmd;
import com.gpse.sesam.web.cmd.SesamUserCmd;
import com.gpse.sesam.web.cmd.UpdatePasswordCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

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

	@GetMapping("/user")
	public List<SesamUser> getCurrentUsers() {
		return service.getUsers();
	}

}
