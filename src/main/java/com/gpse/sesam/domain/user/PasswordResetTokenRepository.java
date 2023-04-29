package com.gpse.sesam.domain.user;


import com.gpse.sesam.domain.user.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByToken(String token);
}