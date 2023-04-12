package com.gpse.sesam.domain;

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

    private void ensureUserCmdValid(SesamUserCmd userCmd) throws UnprocessableEntityException {
        final String email = userCmd.getEmail();

        if (email == null || !email.matches("[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+")) {
            throw new UnprocessableEntityException();
        }

        if (userCmd.getPassword() == null) {
            throw new UnprocessableEntityException();
        }

        final String firstName = userCmd.getFirstName();

        if (firstName == null || firstName.length() < 1) {
            throw new UnprocessableEntityException();
        }

        final String lastName = userCmd.getLastName();

        if (lastName == null || lastName.length() < 1) {
            throw new UnprocessableEntityException();
        }

        if (userCmd.getRequestedRoles() == null) {
            throw new UnprocessableEntityException();
        }
    }

    @Override
    public SesamUser createUser(SesamUserCmd userCmd) throws DataIntegrityViolationException,
            UnprocessableEntityException {
        ensureUserCmdValid(userCmd);

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

        return repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username + " not found."));
    }
}
