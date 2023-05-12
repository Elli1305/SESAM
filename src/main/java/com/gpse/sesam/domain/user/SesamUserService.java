package com.gpse.sesam.domain.user;

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

    List<SesamUser> getUsers();

    SesamUser getUserByMail(String username);

    void makeUserEdit(SesamUser user, String prename, String lastname, String mail, List<SesamUserRole.AttainableRole> roles);

    void deleteAll();

    void deleteUser(SesamUser sesamUser);

    void saveAll(Iterable<SesamUser> users);

    void createPasswordResetToken(SesamUser user, String token);

    void updatePasswordWithToken(String token, String password);

    void changePassword(SesamUser user, String password);
}
