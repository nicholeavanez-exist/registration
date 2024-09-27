package com.ecc.nichole.registration.core.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class ContactInformation {

    private String landline;

    private String mobileNumber;

    @Email(message = "Email should be valid")
    private String email;

    private ContactInformation(Builder builder) {
        this.landline = builder.landline;
        this.mobileNumber = builder.mobileNumber;
        this.email = builder.email;
    }

    public ContactInformation() {}

    public String getLandline() {
        return landline;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public static class Builder {
        private String landline;
        private String mobileNumber;
        private String email;

        public Builder landline(String landline) {
            this.landline = landline;
            return this;
        }

        public Builder mobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public ContactInformation build() {
            return new ContactInformation(this);
        }
    }

    public ContactInformation.Builder toBuilder() {
        return new Builder()
            .email(this.email)
            .mobileNumber(this.mobileNumber)
            .landline(this.landline);
    }
}