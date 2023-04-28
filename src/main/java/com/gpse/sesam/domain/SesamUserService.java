package com.gpse.sesam.domain;

import com.gpse.sesam.web.cmd.SesamUserCmd;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * Interface for interacting with {@link SesamUser}
 */
public interface SesamUserService extends UserDetailsService {
    /**
     * Creates a new {@link SesamUser} based on the provided values.
     *
     * @param userCmd the {@link SesamUserCmd} containing the information needed to create the new user.
     * @return the newly created user.
     */
    SesamUser createUser(SesamUserCmd userCmd);

	void deleteAll();

	void saveAll(Iterable<SesamUser> users);
}
