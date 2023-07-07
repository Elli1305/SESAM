package com.gpse.sesam.domain.user;

import com.gpse.sesam.domain.mail.MailInformation;
import com.gpse.sesam.domain.mail.MailService;
import com.gpse.sesam.domain.user.issuer.Issuer;
import com.gpse.sesam.domain.user.issuer.IssuerService;
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
import org.springframework.util.StringUtils;

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
	private IssuerService issuerService;

	@Autowired
	public SesamUserServiceImpl(final SesamUserRepository userRepository,
								final PasswordResetTokenRepository passwordResetTokenRepository,
								final PasswordEncoder passwordEncoder, final MailService mailService,
								final MessageSource messageSource, final IssuerService issuerService) {
		this.userRepository = userRepository;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
		this.passwordEncoder = passwordEncoder;
		this.mailService = mailService;
		this.messageSource = messageSource;
		this.issuerService = issuerService;
	}

	private static void validateRoles(final List<SesamUserRole.AttainableRole> roles) {
		if (roles == null) {
			throw new UnprocessableEntityException("roles may not be null");
		}
	}

	private static void validateName(final String name, final String propertyName) {
		if (!StringUtils.hasText(name) || name.matches(NUMBER_REGEX)) {
			throw new UnprocessableEntityException(propertyName + " does not meet the requirements");
		}
	}

	private static void validateEmail(final String email) {
		if (email == null || !email.matches("[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+")) {
			throw new UnprocessableEntityException("The provided e-mail is not valid");
		}
	}

	@Override
	public SesamUser createUser(final SesamUserCmd userCmd) {
		validateUser(userCmd);

		final SesamUser user = new SesamUser(
				userCmd.getEmail(),
				passwordEncoder.encode(userCmd.getPassword()),
				userCmd.getFirstName(),
				userCmd.getLastName(),
				userCmd.getRequestedRoles().stream().distinct().map(SesamUserRole::new)
						.toList()
		);

		try {
			return userRepository.save(user);
		} catch (final DataIntegrityViolationException e) {
			throw new ConflictException("A user with that e-mail address already exists.", e);
		}
	}

	private void validateUser(final SesamUserCmd userCmd) {
		validatePassword(userCmd.getPassword());
		validateEmail(userCmd.getEmail());
		validateName(userCmd.getFirstName(), "firstName");
		validateName(userCmd.getLastName(), "lastName");
		validateRoles(userCmd.getRequestedRoles());
	}

	@Override
	public UserDetails loadUserByUsername(final String username) {
		return userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + " not found."));
	}

	@Override
	public void createPasswordResetToken(final SesamUser user, final String token) {
		final PasswordResetToken resetToken = new PasswordResetToken(user, token);

		passwordResetTokenRepository.save(resetToken);

		final Locale locale = LocaleContextHolder.getLocale();

		mailService.send(new MailInformation("gp.se.team.3.1@gmail.com", user.getUsername(),
				messageSource.getMessage("reset" + ".subject", null, locale),
				messageSource.getMessage("reset.text",
						new String[]{user.getFirstName(), user.getLastName(), token}, locale)));
	}

	@Override
	public void updatePasswordWithToken(final String token, final String password) {
		final PasswordResetToken passwordResetToken = passwordResetTokenRepository
				.findByToken(token)
				.orElseThrow(() -> new InvalidTokenException("token does not exist."));

		if (passwordResetToken.isExpired()) {
			passwordResetTokenRepository.delete(passwordResetToken);
			throw new InvalidTokenException("token is expired");
		}

		validatePassword(password);

		final SesamUser user = passwordResetToken.getUser();
		changePassword(user, password);

		passwordResetTokenRepository.delete(passwordResetToken);
	}

	@Override
	public void changePassword(final SesamUser user, final String password) {
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}

	private void validatePassword(final String password) {
		if (password == null || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])"
				+ "[a-zA-Z0-9!@#$%^&*]{8,120}$")) {
			throw new UnprocessableEntityException("password doesn't match the required criteria");
		}
	}

	@Override
	public void deleteUser(final SesamUser sesamUser) {
		userRepository.delete(sesamUser);
	}

	@Override
	public void saveAll(final Iterable<SesamUser> users) {
		userRepository.saveAll(users);
	}

	@Override
	public List<SesamUser> getUsers() {
		final List<SesamUser> articles = new ArrayList<>();
		userRepository.findAll().forEach(articles::add);
		return articles;
	}

	@Override
	public SesamUser getUserByMail(final String username) {
		return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username
				+ " not found."));
	}

	@Override
	public void makeUserEdit(final SesamUser user, final String prename, final String lastname, final String username,
							 final List<SesamUserRole.AttainableRole> roles) {
		if (roles.contains(SesamUserRole.AttainableRole.ISSUER)) {
			userRepository.delete(user);
			Issuer issuer = new Issuer(user.getUsername(), username, prename, lastname, roles.stream()
					.distinct().map(role -> new SesamUserRole(role, true))
					.collect(Collectors.toList()), null);
			issuerService.save(issuer);
		} else {
			user.setLastName(lastname);
			user.setFirstName(prename);
			user.setRoles(roles.stream().distinct().map(role -> new SesamUserRole(role, true))
				.collect(Collectors.toList()));
			userRepository.save(user);
		}

	}
}
