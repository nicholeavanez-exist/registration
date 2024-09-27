package com.ecc.nichole.registration.core.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "person_role",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public Person() {
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
    }
    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public double getGwa() {
        return gwa;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public boolean isEmployed() {
        return employed;
    }

    public ContactInformation getContactInformation() {
        return contactInformation;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        this.updatedAt = ZonedDateTime.now();
    }

    public void setGwa(double gwa) {
        this.gwa = gwa;
        this.updatedAt = ZonedDateTime.now();
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
        this.updatedAt = ZonedDateTime.now();
    }

    public void setEmployed(boolean employed) {
        this.employed = employed;
        this.updatedAt = ZonedDateTime.now();
    }

    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
        this.updatedAt = ZonedDateTime.now();
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
        this.updatedAt = ZonedDateTime.now();
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