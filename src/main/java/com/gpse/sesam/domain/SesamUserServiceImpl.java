package com.gpse.sesam.domain;

import com.gpse.sesam.web.ConflictException;
import com.gpse.sesam.web.cmd.SesamUserCmd;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SesamUserServiceImpl implements SesamUserService {
	private final SesamUserRepository repository;

	private final PasswordEncoder passwordEncoder;

	public SesamUserServiceImpl(final SesamUserRepository repository, final PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public SesamUser createUser(SesamUserCmd userCmd) throws ConflictException {
		final SesamUser user = new SesamUser(
				userCmd.getEmail(),
				passwordEncoder.encode(userCmd.getPassword()),
				userCmd.getFirstName(),
				userCmd.getLastName(),
				userCmd.getRoles().stream()
						.map(SesamUserRole::new)
						.toList()
		);

		try {
			return repository.save(user);
		} catch (DataIntegrityViolationException e) {
			throw new ConflictException();
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + " not found."));
	}
}
