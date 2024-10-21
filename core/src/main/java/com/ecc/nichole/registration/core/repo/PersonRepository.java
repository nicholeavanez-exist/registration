package com.ecc.nichole.registration.core.repo;

import com.ecc.nichole.registration.core.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    boolean existsByUuid(UUID uuid);

    @Transactional
    void deleteByUuid(UUID uuid);

    Optional<Person> findByUuid(UUID uuid);
}