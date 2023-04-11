package com.gpse.sesam.domain;

import com.gpse.sesam.web.cmd.SesamUserCmd;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 */
public interface SesamUserService extends UserDetailsService {
    /**
     *
     * @param email
     * @param password
     * @param firstName
     * @param lastName
     * @return
     */
    SesamUser createUser(SesamUserCmd userCmd);
}
