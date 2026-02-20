package com.auth.usermanagement.domain.port.out;

import com.common.shared.domain.model.Status;
import com.auth.usermanagement.domain.model.user.UserDomain;
import com.auth.usermanagement.domain.model.user.vo.UserEmail;
import com.auth.usermanagement.domain.model.user.vo.UserId;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryPort {
    UserDomain save(UserDomain user);

    Optional<UserDomain> findById(UserId id);

    Optional<UserDomain> findByEmail(UserEmail email);

    List<UserDomain> findAll();

    boolean existsById(UserId id);

    void updateEnabled(UserId id, boolean enabled);

    Page<UserDomain> searchByUsernameOrUserId(String email, Status status, Pageable pageable);
}
