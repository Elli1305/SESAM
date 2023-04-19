package com.gpse.sesam.domain;

import com.gpse.sesam.web.ConflictException;
import com.gpse.sesam.web.UnprocessableEntityException;
import com.gpse.sesam.web.cmd.SesamUserCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class SesamUserServiceImpl implements SesamUserService {
    private static final String NUMBER_REGEX = "[0-9]";

    private final SesamUserRepository userRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SesamUserServiceImpl(final SesamUserRepository userRepository,
                                final PasswordResetTokenRepository passwordResetTokenRepository,
                                final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SesamUser createUser(SesamUserCmd userCmd) throws ConflictException, UnprocessableEntityException {
        final String email = userCmd.getEmail();

        if (email == null || !email.matches("[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+")) {
            throw new UnprocessableEntityException("The provided e-mail is not valid");
        }

        final String password = userCmd.getPassword();

        if (password == null || !password.matches("^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,120}$")) {
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
    }

    @Override
    public void updatePasswordWithToken(String token, String password) throws UnprocessableEntityException {
        final PasswordResetToken passwordResetToken = passwordResetTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new UnprocessableEntityException("token does not exist."));

        if (passwordResetToken.isExpired()) {
            passwordResetTokenRepository.delete(passwordResetToken);
            throw new UnprocessableEntityException("token is expired");
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
}
