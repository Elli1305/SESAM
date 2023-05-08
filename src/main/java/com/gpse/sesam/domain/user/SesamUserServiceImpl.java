package com.gpse.sesam.domain.user;

import com.gpse.sesam.domain.mail.MailService;
import com.gpse.sesam.web.cmd.SesamUserCmd;
import com.gpse.sesam.web.exception.ConflictException;
import com.gpse.sesam.web.exception.InvalidTokenException;
import com.gpse.sesam.web.exception.UnprocessableEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class SesamUserServiceImpl implements SesamUserService {
	private static final String NUMBER_REGEX = "[0-9]+";

	private final SesamUserRepository userRepository;

	private final PasswordResetTokenRepository passwordResetTokenRepository;

	private final PasswordEncoder passwordEncoder;

	private final MailService mailService;

	private final MessageSource messageSource;

	@Autowired
	public SesamUserServiceImpl(final SesamUserRepository userRepository,
								final PasswordResetTokenRepository passwordResetTokenRepository,
								final PasswordEncoder passwordEncoder,
								final MailService mailService,
								final MessageSource messageSource) {
		this.userRepository = userRepository;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
		this.passwordEncoder = passwordEncoder;
		this.mailService = mailService;
		this.messageSource = messageSource;
	}

	@Override
	public SesamUser createUser(SesamUserCmd userCmd) throws ConflictException, UnprocessableEntityException {
		final String email = userCmd.getEmail();

		if (email == null || !email.matches("[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+")) {
			throw new UnprocessableEntityException("The provided e-mail is not valid");
		}

		final String password = userCmd.getPassword();

		if (isInvalidPassword(password)) {
			throw new UnprocessableEntityException("password doesn't match the required criteria");
		}

		final String firstName = userCmd.getFirstName();

		if (firstName == null || firstName.isBlank() || firstName.matches(NUMBER_REGEX)) {
			throw new UnprocessableEntityException("firstName does not meet the requirements");
		}

		final String lastName = userCmd.getLastName();

		if (lastName == null || lastName.isBlank() || lastName.matches(NUMBER_REGEX)) {
			throw new UnprocessableEntityException("lastName does not meet the requirements");
		}

		if (userCmd.getRequestedRoles() == null) {
			throw new UnprocessableEntityException("roles may not be null");
		}

		final SesamUser user = new SesamUser(
				email,
				passwordEncoder.encode(password),
				firstName,
				lastName,
				userCmd.getRequestedRoles().stream()
						.distinct()
						.map(SesamUserRole::new)
						.collect(Collectors.toList())
		);

		try {
			return userRepository.save(user);
		} catch (DataIntegrityViolationException e) {
			throw new ConflictException("A user with that e-mail address already exists.", e);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + " not found."));
	}

	@Override
	public void createPasswordResetToken(SesamUser user, String token) {
		PasswordResetToken resetToken = new PasswordResetToken(user, token);

		passwordResetTokenRepository.save(resetToken);

		Locale locale = LocaleContextHolder.getLocale();

		mailService.send(
				"gp.se.team.3.1@gmail.com",
				user.getUsername(),
				messageSource.getMessage("reset.subject", null, locale),
				messageSource.getMessage(
						"reset.text",
						new String[]{
								user.getFirstName(),
								user.getLastName(),
								token,
						},
						locale
				)
		);
	}

	@Override
	public void updatePasswordWithToken(String token, String password) throws UnprocessableEntityException {
		final PasswordResetToken passwordResetToken = passwordResetTokenRepository
				.findByToken(token)
				.orElseThrow(() -> new InvalidTokenException("token does not exist."));

		if (passwordResetToken.isExpired()) {
			passwordResetTokenRepository.delete(passwordResetToken);
			throw new InvalidTokenException("token is expired");
		}

		if (isInvalidPassword(password)) {
			throw new UnprocessableEntityException("password doesn't match the required criteria");
		}

		SesamUser user = passwordResetToken.getUser();
		changePassword(user, password);

		passwordResetTokenRepository.delete(passwordResetToken);
	}

	@Override
	public void changePassword(SesamUser user, String password) {
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}

	private boolean isInvalidPassword(String password) {
		return password == null || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])"
				+ "[a-zA-Z0-9!@#$%^&*]{8,120}$");
	}

	@Override
	public void deleteAll() {
		userRepository.deleteAll();
	}

	@Override
	public void saveAll(Iterable<SesamUser> users) {
		userRepository.saveAll(users);
	}
	@Override
	public List<SesamUser> getUsers() {
		final List<SesamUser> articles = new ArrayList<>();
		userRepository.findAll().forEach(articles::add);
		return articles;
	}
}
