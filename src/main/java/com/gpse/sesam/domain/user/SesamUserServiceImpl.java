package com.gpse.sesam.domain.user;

import com.gpse.sesam.domain.credential.credentials.Credential;
import com.gpse.sesam.domain.credential.credentials.internal.CredentialService;
import com.gpse.sesam.domain.credential.credentials.internal.InternalCredential;
import com.gpse.sesam.domain.mail.MailInformation;
import com.gpse.sesam.domain.mail.MailService;
import com.gpse.sesam.domain.user.issuer.Issuer;
import com.gpse.sesam.domain.user.issuer.IssuerService;
import com.gpse.sesam.web.cmd.SesamUserCmd;
import com.gpse.sesam.web.exception.ConflictException;
import com.gpse.sesam.web.exception.InvalidTokenException;
import com.gpse.sesam.web.exception.UnprocessableEntityException;
import io.swagger.v3.oas.models.info.License;
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

/**
 * Der SesamUserServiceImpl ist ein Service für die Verwaltung von Benutzern.
 * Die Klasse implementiert das SesamUserService-Interface.
 */
@Service
public class SesamUserServiceImpl implements SesamUserService {
	private static final String NUMBER_REGEX = "[0-9]+";

	private final SesamUserRepository userRepository;

	private final PasswordResetTokenRepository passwordResetTokenRepository;

	private final PasswordEncoder passwordEncoder;
	private final MailService mailService;

	private final MessageSource messageSource;
	private IssuerService issuerService;
	private CredentialService credentialService;

	/**
	 * Erstellt eine neue Instanz des SesamUserServiceImpl und initialisiert die Repositories, den PasswordEncoder,
	 * den MailService, die MessageSource und den IssuerService.
	 *
	 * @param userRepository               	das Repository für die Benutzer
	 * @param passwordResetTokenRepository 	das Repository für die Passwort-Zurücksetzungstoken
	 * @param passwordEncoder              	der PasswordEncoder zum Verschlüsseln der Passwörter
	 * @param mailService                  	der MailService zum Versenden von E-Mails
	 * @param messageSource               	die MessageSource für die internationalisierten Nachrichten
	 * @param issuerService                	der IssuerService für die Verwaltung von Ausstellern
	 * @param credentialService				der CredentialService zum Verwalten der Abhängigkeiten von Credentials
	 */
	@Autowired
	public SesamUserServiceImpl(final SesamUserRepository userRepository,
								final PasswordResetTokenRepository passwordResetTokenRepository,
								final PasswordEncoder passwordEncoder, final MailService mailService,
								final MessageSource messageSource, final IssuerService issuerService,
								final CredentialService credentialService) {
		this.userRepository = userRepository;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
		this.passwordEncoder = passwordEncoder;
		this.mailService = mailService;
		this.messageSource = messageSource;
		this.issuerService = issuerService;
		this.credentialService = credentialService;
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

	/**
	 * Überprüft die Gültigkeit der übergebenen Benutzerdaten und erstellt einen neuen Benutzer.
	 * Wirft eine ConflictException, wenn bereits ein Benutzer mit der angegebenen E-Mail-Adresse existiert.
	 *
	 * @param userCmd die Benutzerdaten
	 * @return der erstellte Benutzer
	 */
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

	/**
	 * Überprüft, ob ein Benutzer mit dem angegebenen Benutzernamen existiert und gibt einen UserDetails-Objekt zurück.
	 * Wirft eine UsernameNotFoundException, wenn kein Benutzer mit dem angegebenen Benutzernamen gefunden wurde.
	 *
	 * @param username der Benutzername
	 * @return das UserDetails-Objekt des Benutzers
	 * @throws UsernameNotFoundException wenn kein Benutzer mit dem angegebenen Benutzernamen gefunden wurde
	 */
	@Override
	public UserDetails loadUserByUsername(final String username) {
		return userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + " not found."));
	}

	/**
	 * Erstellt ein PasswordResetToken für den angegebenen Benutzer und sendet eine E-Mail mit dem Token.
	 *
	 * @param user  der Benutzer
	 * @param token das PasswordResetToken
	 */
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

	/**
	 * Aktualisiert das Passwort des Benutzers mit dem angegebenen Token.
	 * Wirft eine InvalidTokenException, wenn das Token ungültig oder abgelaufen ist.
	 *
	 * @param token    das Token
	 * @param password das neue Passwort
	 * @throws InvalidTokenException wenn das Token ungültig oder abgelaufen ist
	 */
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

	/**
	 * Ändert das Passwort des angegebenen Benutzers.
	 *
	 * @param user     der Benutzer
	 * @param password das neue Passwort
	 */
	@Override
	public void changePassword(final SesamUser user, final String password) {
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}

	/**
	 * Überprüft das Passwort auf Gültigkeit.
	 *
	 * @param password das zu überprüfende Passwort
	 * @throws UnprocessableEntityException wenn das Passwort nicht den Anforderungen entspricht
	 */
	private void validatePassword(final String password) {
		if (password == null || !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])"
				+ "[a-zA-Z0-9!@#$%^&*]{8,120}$")) {
			throw new UnprocessableEntityException("password doesn't match the required criteria");
		}
	}

	/**
	 * Löscht einen Benutzer.
	 *
	 * @param sesamUser der zu löschende Benutzer
	 */
	@Override
	public void deleteUser(final SesamUser sesamUser) {
		if (sesamUser instanceof Issuer) {
			List<InternalCredential> credentials = credentialService.getCredentialsByIssuerId(sesamUser.getId());
			for (InternalCredential credential : credentials) {
				credential.removeIssuer((Issuer) sesamUser);
			}
			credentialService.saveAll(credentials);
		}
		userRepository.delete(sesamUser);
	}

	/**
	 * Speichert eine Liste von Benutzern.
	 *
	 * @param users die zu speichernden Benutzer
	 */
	@Override
	public void saveAll(final Iterable<SesamUser> users) {
		userRepository.saveAll(users);
	}

	/**
	 * Gibt eine Liste aller Benutzer zurück.
	 *
	 * @return die Liste der Benutzer
	 */
	@Override
	public List<SesamUser> getUsers() {
		final List<SesamUser> articles = new ArrayList<>();
		userRepository.findAll().forEach(articles::add);
		return articles;
	}

	/**
	 * Gibt einen Benutzer anhand seiner E-Mail-Adresse zurück.
	 *
	 * @param username die E-Mail-Adresse des Benutzers
	 * @return der gefundene Benutzer
	 * @throws UsernameNotFoundException wenn kein Benutzer mit der angegebenen E-Mail-Adresse gefunden wurde
	 */
	@Override
	public SesamUser getUserByMail(final String username) {
		return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username
				+ " not found."));
	}

	/**
	 * Aktualisiert die Informationen eines Benutzers.
	 *
	 * @param user     der zu aktualisierende Benutzer
	 * @param prename  der neue Vorname
	 * @param lastname der neue Nachname
	 * @param username der neue Benutzername
	 * @param roles    die neuen Rollen
	 */
	@Override
	public void makeUserEdit(final SesamUser user, final String prename, final String lastname, final String username,
							 final List<SesamUserRole.AttainableRole> roles) {
		if (roles.contains(SesamUserRole.AttainableRole.ISSUER) && !(user instanceof Issuer)) {
			deleteUser(user);
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
