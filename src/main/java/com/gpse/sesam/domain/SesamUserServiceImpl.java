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
    private final SesamUserRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SesamUserServiceImpl(final SesamUserRepository repository, final PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    private void validateUserCmd(SesamUserCmd userCmd) throws UnprocessableEntityException {
        final String email = userCmd.getEmail();

        if (email == null || !email.matches("[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+")) {
            throw new UnprocessableEntityException("The provided e-mail is not valid");
        }

        final String password = userCmd.getPassword();

        if (password == null || password.isBlank()) {
            throw new UnprocessableEntityException("password may not be empty");
        }

        final String firstName = userCmd.getFirstName();

        if (firstName == null || firstName.isBlank()) {
            throw new UnprocessableEntityException("firstName may not be empty");
        }

        final String lastName = userCmd.getLastName();

        if (lastName == null || lastName.isBlank()) {
            throw new UnprocessableEntityException("lastName may not be empty");
        }

        if (userCmd.getRequestedRoles() == null) {
            throw new UnprocessableEntityException("roles may not be null");
        }
    }

    @Override
    public SesamUser createUser(SesamUserCmd userCmd) throws DataIntegrityViolationException,
            UnprocessableEntityException {
        validateUserCmd(userCmd);

        final SesamUser user = new SesamUser(
                userCmd.getEmail(),
                passwordEncoder.encode(userCmd.getPassword()),
                userCmd.getFirstName(),
                userCmd.getLastName(),
                userCmd.getRequestedRoles().stream()
                        .distinct()
                        .map(SesamUserRole::new)
                        .collect(Collectors.toList())
        );

        try {
            return repository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username + " not found."));
    }
}
