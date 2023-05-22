package com.gpse.sesam.domain.user;

import com.gpse.sesam.domain.mail.MailInformation;
import com.gpse.sesam.domain.mail.MailService;
import com.gpse.sesam.web.cmd.SesamUserCmd;
import com.gpse.sesam.web.exception.ConflictException;
import com.gpse.sesam.web.exception.InvalidTokenException;
import com.gpse.sesam.web.exception.UnprocessableEntityException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class SesamUserServiceImplTest {

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private SesamUserRepository sesamUserRepository;

	@Mock
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Mock
	private MailService mailService;

	@Mock
	private MessageSource messageSource;

	@InjectMocks
	private SesamUserServiceImpl sesamUserService;

	@Captor
	private ArgumentCaptor<SesamUser> userCaptor;

	@Captor
	private ArgumentCaptor<List<SesamUser>> userListCaptor;

	private AutoCloseable autoCloseable;

	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}

	@Test
	void createUserShouldCallRepositoryWithCorrectUser() {
		SesamUserCmd userCmd = new SesamUserCmd("johndoe@example.com", "Hallo123!", "John", "Doe",
				Collections.singletonList(SesamUserRole.AttainableRole.ADMINISTRATOR));

		when(passwordEncoder.encode(userCmd.getPassword())).thenReturn("hashedPassword");

		sesamUserService.createUser(userCmd);

		verify(sesamUserRepository).save(userCaptor.capture());

		SesamUser user = userCaptor.getValue();

		assertThat(user, notNullValue());
		assertThat(user.getUsername(), is(userCmd.getEmail()));
		assertThat(user.getPassword(), is("hashedPassword"));
		assertThat(user.getFirstName(), is(userCmd.getFirstName()));
		assertThat(user.getLastName(), is(userCmd.getLastName()));
		assertThat(user.getRoles().size(), is(1));
		assertThat(user.getRoles().get(0).isGranted(), is(false));
		assertThat(user.getRoles().get(0).getRole(), is(SesamUserRole.AttainableRole.ADMINISTRATOR));
	}

	@ParameterizedTest
	@ValueSource(strings = {"", " ", "12", "1"})
	@NullSource
	void createUserShouldRaiseExceptionIfFirstNameIsNotCorrect(String firstName) {
		SesamUserCmd userCmd = new SesamUserCmd("johndoe@example.com", "Hallo123!", firstName, "Doe",
				Collections.singletonList(SesamUserRole.AttainableRole.ADMINISTRATOR));

		assertThrows(UnprocessableEntityException.class, () -> sesamUserService.createUser(userCmd), "firstName does" +
				"not meet the requirements");
		verifyNoInteractions(sesamUserRepository);
	}

	@ParameterizedTest
	@ValueSource(strings = {"", " ", "12", "1"})
	@NullSource
	void createUserShouldRaiseExceptionIfLastNameIsNotCorrect(String lastName) {
		SesamUserCmd userCmd = new SesamUserCmd("johndoe@example.com", "Hallo123!", "John", lastName,
				Collections.singletonList(SesamUserRole.AttainableRole.ADMINISTRATOR));

		assertThrows(UnprocessableEntityException.class, () -> sesamUserService.createUser(userCmd), "lastName does " +
				"not meet the requirements");
		verifyNoInteractions(sesamUserRepository);
	}

	@ParameterizedTest
	@ValueSource(strings = {"", " ", "12345678", "hallo123!", "Hallo1234", "Hao123!", "Hallooo!"})
	@NullSource
	void createUserShouldRaiseExceptionIfPasswordIsNotCorrect(String password) {
		SesamUserCmd userCmd = new SesamUserCmd("johndoe@example.com", password, "John", "Doe",
				Collections.singletonList(SesamUserRole.AttainableRole.ADMINISTRATOR));

		assertThrows(UnprocessableEntityException.class, () -> sesamUserService.createUser(userCmd), "password does " +
				"not meet the requirements");
		verifyNoInteractions(sesamUserRepository);
	}

	@Test
	void createUserShouldRaiseExceptionIfEMailIsNotCorrect() {
		SesamUserCmd userCmd = new SesamUserCmd("johndoeexample.com", "Hallo123!", "John", "Doe",
				Collections.singletonList(SesamUserRole.AttainableRole.ADMINISTRATOR));

		assertThrows(UnprocessableEntityException.class, () -> sesamUserService.createUser(userCmd), "The provided " +
				"e-mail is not valid");
		verifyNoInteractions(sesamUserRepository);
	}

	@Test
	void createUserShouldRaiseExceptionIfRolesAreNull() {
		SesamUserCmd userCmd = new SesamUserCmd("johndoe@example.com", "Hallo123!", "John", "Doe",
				null);

		assertThrows(UnprocessableEntityException.class, () -> sesamUserService.createUser(userCmd), "roles may not " +
				"be null");
		verifyNoInteractions(sesamUserRepository);
	}

	@Test
	void createUserShouldRaiseConflictExceptionIfSaveRaisesAnException() {
		SesamUserCmd userCmd = new SesamUserCmd("johndoe@example.com", "Hallo123!", "John", "Doe",
				Collections.singletonList(SesamUserRole.AttainableRole.ADMINISTRATOR));


		when(sesamUserRepository.save(any())).thenThrow(DataIntegrityViolationException.class);
		assertThrows(ConflictException.class, () -> sesamUserService.createUser(userCmd), "A user with that e-mail " +
				"address already exists.");

	}

	@ParameterizedTest
	@ValueSource(strings = {"", " ", "12345678", "hallo123!", "Hallo1234", "Hao123!", "Hallooo!"})
	@NullSource
	void updatePasswordWithTokenShouldRaiseExceptionIfPasswordDoesNotMatchCriteria(String password) {
		String token = "token";
		when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.of(new PasswordResetToken(new SesamUser(), token)));

		assertThrows(UnprocessableEntityException.class, () -> sesamUserService.updatePasswordWithToken(token,
				password), "password doesn't match the required criteria");
		verify(passwordResetTokenRepository, atMostOnce()).findByToken(token);
		verifyNoInteractions(sesamUserRepository);
	}

	@Test
	void updatePasswordWithTokenShouldDeleteTokenAndUpdatePassword() {
		String tokenString = "token";
		String password = "Password123!";
		PasswordResetToken token = new PasswordResetToken(new SesamUser(), tokenString);
		when(passwordResetTokenRepository.findByToken(tokenString)).thenReturn(Optional.of(token));
		when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

		sesamUserService.updatePasswordWithToken(tokenString, password);

		verify(passwordResetTokenRepository).delete(token);
		verify(sesamUserRepository).save(userCaptor.capture());

		assertThat(userCaptor.getValue().getPassword(), is("encodedPassword"));
	}

	@Test
	void updatePasswordWithTokenShouldRaiseExceptionWhenTokenIsInvalid() {
		String token = "token";
		when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.empty());

		assertThrows(InvalidTokenException.class, () -> sesamUserService.updatePasswordWithToken(token,
				"password"), "token does not exist.");

		verifyNoInteractions(sesamUserRepository);
	}

	@Test
	void updatePasswordWithTokenShouldRaiseExceptionWhenTokenIsExpired() {
		String token = "token";
		PasswordResetToken resetToken = new PasswordResetToken(new SesamUser(), token);
		ReflectionTestUtils.setField(resetToken, "expiryDate", new Date());

		when(passwordResetTokenRepository.findByToken(token)).thenReturn(Optional.of(resetToken));

		assertThrows(InvalidTokenException.class, () -> sesamUserService.updatePasswordWithToken(token,
				"password"), "token is expired");

		verifyNoInteractions(sesamUserRepository);
		verify(passwordResetTokenRepository).delete(resetToken);
	}

	@Test
	void deleteAllShouldCallRepository() {
		sesamUserService.deleteAll();

		// assert
		verify(sesamUserRepository).deleteAll();
	}

	@Test
	void saveAllShouldCallRepositoryWithCorrectArguments() {
		List<SesamUser> users = Collections.singletonList(new SesamUser());

		sesamUserService.saveAll(users);

		verify(sesamUserRepository).saveAll(userListCaptor.capture());

		assertThat(userListCaptor.getValue(), is(users));
	}

	@Test
	void getUsersShouldReturnListOfLocations() {
		when(sesamUserRepository.findAll()).thenReturn(Collections.singletonList(new SesamUser()));

		List<SesamUser> users = sesamUserService.getUsers();

		assertThat(users.size(), is(1));
	}


	@Test
	void creatPasswordResetTokenShouldCallMailService() {
		SesamUser sesamUser = new SesamUser();
		sesamUser.setFirstName("firstName");
		sesamUser.setLastName("lastName");
		sesamUser.setEmail("email");

		String token = "token";

		LocaleContextHolder.setLocale(Locale.GERMAN);

		when(messageSource.getMessage("reset.subject", null, LocaleContextHolder.getLocale())).thenReturn("subject");
		when(messageSource.getMessage("reset.text", new String[]{sesamUser.getFirstName(), sesamUser.getLastName(),
				token}, LocaleContextHolder.getLocale())).thenReturn("text");

		sesamUserService.createPasswordResetToken(sesamUser, token);

		verify(passwordResetTokenRepository).save(new PasswordResetToken(sesamUser, token));
		verify(mailService).send(new MailInformation("gp.se.team.3.1@gmail.com", sesamUser.getUsername(), "subject",
				"text"));

	}

	@Test
	void loadUserByUsernameShouldCallRepository() {
		String username = "email";
		SesamUser user = new SesamUser();
		user.setEmail("test");

		when(sesamUserRepository.findByEmail(username)).thenReturn(Optional.of(user));

		UserDetails userDetails = sesamUserService.loadUserByUsername(username);

		assertThat(userDetails.getUsername(), is(user.getUsername()));
	}

	@Test
	void loadUserByUsernameShouldRaiseExceptionWhenUserDoesNotExist() {
		String username = "username";
		when(sesamUserRepository.findByEmail(username)).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () -> sesamUserService.loadUserByUsername(username), "username" +
				" " +
				"not found.");
	}
}