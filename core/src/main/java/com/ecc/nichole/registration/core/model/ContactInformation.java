package com.ecc.nichole.registration.core.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ContactInformation {

    @Pattern(regexp = "^[0-9]{7,9}$", message = "Invalid landline number")
    private String landline;

    @Pattern(regexp = "^\\+?[0-9]{11,12}$", message = "Invalid mobile number")
    private String mobileNumber;

    @Email(message = "Email should be valid")
    private String email;

    private ContactInformation(Builder builder) {
        this.landline = builder.landline;
        this.mobileNumber = builder.mobileNumber;
        this.email = builder.email;
    }

    public ContactInformation() {}

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