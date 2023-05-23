package com.gpse.sesam.web.controller;

import com.gpse.sesam.domain.user.SesamUser;
import com.gpse.sesam.domain.user.SesamUserService;
import com.gpse.sesam.web.cmd.EditUserCmd;
import com.gpse.sesam.web.cmd.PasswordResetCmd;
import com.gpse.sesam.web.cmd.SesamUserCmd;
import com.gpse.sesam.web.cmd.UpdatePasswordCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class SesamUserController {

	public static final String ADMINISTRATOR = "ADMINISTRATOR";
	private final SesamUserService service;

	@Autowired
	public SesamUserController(final SesamUserService service) {
		this.service = service;
	}

	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public SesamUser createUser(@RequestBody final SesamUserCmd sesamUserCmd) {
		return service.createUser(sesamUserCmd);
	}

	@PostMapping("/password_reset")
	@ResponseStatus(HttpStatus.CREATED)
	public void passwordReset(@RequestBody final PasswordResetCmd passwordResetCmd) {
		final SesamUser user = (SesamUser) service.loadUserByUsername(passwordResetCmd.getEmail());
		final String token = UUID.randomUUID().toString();

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

	@Secured(ADMINISTRATOR)
	@GetMapping("/user")
	public List<SesamUser> getCurrentUsers() {
		return service.getUsers();
	}

	@Secured(ADMINISTRATOR)
	@GetMapping("/user/edit/{id}")
	public SesamUser getUserToEdit(@PathVariable("id") final String id) {
		return service.getUserByMail(id);
	}

	@Secured(ADMINISTRATOR)
	@PostMapping("/edit_user")
	@ResponseStatus(HttpStatus.OK)
	public void makeUserEdit(@RequestBody final EditUserCmd editUserCmd) {
		final SesamUser user = service.getUserByMail(editUserCmd.getUsername());
		service.makeUserEdit(user,
				editUserCmd.getFirstName(),
				editUserCmd.getLastName(),
				editUserCmd.getUsername(),
				editUserCmd.getRoles());
	}

	@Secured(ADMINISTRATOR)
	@DeleteMapping("/delete_user/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteUser(@PathVariable("id") final String id) {
		final SesamUser user = service.getUserByMail(id);
		service.deleteUser(user);
	}

}
