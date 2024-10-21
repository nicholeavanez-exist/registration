package com.ecc.nichole.registration.core.model.dto;

import com.ecc.nichole.registration.core.model.Address;
import com.ecc.nichole.registration.core.model.ContactInformation;
import com.ecc.nichole.registration.core.model.Name;
import com.ecc.nichole.registration.core.model.Role;
import jakarta.persistence.Embedded;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {
    private Long id;

    private UUID uuid;

    @Embedded
    @Valid
    private Name name;

    @Embedded
    private Address address;

    private LocalDate birthDate;

    private double gwa;

    private LocalDate hireDate;

    private boolean employed;

    @Embedded
    @Valid
    private ContactInformation contactInformation;

    private Set<Role> roles = new HashSet<>();

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
