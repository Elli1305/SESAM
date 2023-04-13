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

    private final SesamUserRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SesamUserServiceImpl(final SesamUserRepository repository, final PasswordEncoder passwordEncoder) {
        this.repository = repository;
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
            return repository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("A user with that e-mail address already exists.", e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username + " not found."));
    }
}
