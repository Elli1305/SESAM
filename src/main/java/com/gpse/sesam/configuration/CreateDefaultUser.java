package com.gpse.sesam.configuration;

import com.gpse.sesam.domain.user.SesamUser;
import com.gpse.sesam.domain.user.SesamUserRole;
import com.gpse.sesam.domain.user.SesamUserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateDefaultUser implements InitializingBean {
    private final PasswordEncoder passwordEncoder;

    private final SesamUserService userService;

    public CreateDefaultUser(final PasswordEncoder passwordEncoder, final SesamUserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public void afterPropertiesSet() {
        final SesamUser user = new SesamUser(
                "user@sesam.gp-se.de",
                passwordEncoder.encode("password"),
                "Default",
                "User",
                List.of(
                        new SesamUserRole(SesamUserRole.AttainableRole.ADMINISTRATOR, true),
                        new SesamUserRole(SesamUserRole.AttainableRole.EDITOR, true)
                )
        );

        userService.saveAll(List.of(user));
    }
}
