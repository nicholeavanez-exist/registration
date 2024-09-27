package com.ecc.nichole.registration.core.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class Name {

    @NotNull(message = "Last name cannot be null")
    private String lastName;

    @NotNull(message = "First name cannot be null")
    private String firstName;

    private String middleName;
    private String suffix;
    private String title;

    private Name(Builder builder) {
        this.lastName = builder.lastName;
        this.firstName = builder.firstName;
        this.middleName = builder.middleName;
        this.suffix = builder.suffix;
        this.title = builder.title;
    }

    public Name() {}

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getTitle() {
        return title;
    }

    public static class Builder {
        private String lastName;
        private String firstName;
        private String middleName;
        private String suffix;
        private String title;

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder middleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public Builder suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Name build() {
            return new Name(this);
        }
    }
}