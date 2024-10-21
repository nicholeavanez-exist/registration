package com.ecc.nichole.registration.core.repo;

import com.ecc.nichole.registration.core.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByUuid(UUID uuid);

    @Transactional
    void deleteByUuid(UUID uuid);

    Optional<Role> findByUuid(UUID uuid);
}
