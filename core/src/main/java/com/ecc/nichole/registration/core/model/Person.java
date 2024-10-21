package com.ecc.nichole.registration.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    @Embedded
    private Name name;

    @Embedded
    private Address address;

    private LocalDate birthDate;

    private double gwa;

    private LocalDate hireDate;

    private boolean employed;

    @Embedded
    private ContactInformation contactInformation;

    @ManyToMany(cascade = CascadeType.MERGE)
    private Set<Role> roles = new HashSet<>();

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public Person() {
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
        this.uuid = UUID.randomUUID();
    }

    public void addRole(Role role) {
        this.roles.add(role);
        this.updatedAt = ZonedDateTime.now();
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        this.updatedAt = ZonedDateTime.now();
    }

    public static class Builder {
        private Long id;
        private Name name;
        private Address address;
        private LocalDate birthDate;
        private double gwa;
        private LocalDate hireDate;
        private boolean employed;
        private ContactInformation contactInformation;
        private Set<Role> roles = new HashSet<Role>();
        private ZonedDateTime createdAt;
        private ZonedDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(Name name) {
            this.name = name;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public Builder birthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder gwa(double gwa) {
            this.gwa = gwa;
            return this;
        }

        public Builder hireDate(LocalDate hireDate) {
            this.hireDate = hireDate;
            return this;
        }

        public Builder employed(boolean employed) {
            this.employed = employed;
            return this;
        }

        public Builder contactInformation(ContactInformation contactInformation) {
            this.contactInformation = contactInformation;
            return this;
        }

        public Builder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public Builder createdAt(ZonedDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(ZonedDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Person build() {
            Person person = new Person();
            person.id = this.id;
            person.name = this.name;
            person.address = this.address;
            person.birthDate = this.birthDate;
            person.gwa = this.gwa;
            person.hireDate = this.hireDate;
            person.employed = this.employed;
            person.contactInformation = this.contactInformation;
            person.roles = this.roles;
            person.createdAt = this.createdAt != null ? this.createdAt : ZonedDateTime.now();
            person.updatedAt = this.updatedAt != null ? this.updatedAt : ZonedDateTime.now();
            return person;
        }
    }

    public Builder toBuilder() {
        return new Builder()
            .id(this.id)
            .name(this.name)
            .address(this.address)
            .birthDate(this.birthDate)
            .gwa(this.gwa)
            .hireDate(this.hireDate)
            .employed(this.employed)
            .contactInformation(this.contactInformation)
            .roles(this.roles)
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt);
    }
}