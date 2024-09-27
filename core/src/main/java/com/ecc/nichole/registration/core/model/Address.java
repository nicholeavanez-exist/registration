package com.ecc.nichole.registration.core.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {

    private String streetNumber;
    private String barangay;
    private String city;
    private int zipcode;

    private Address(Builder builder) {
        this.streetNumber = builder.streetNumber;
        this.barangay = builder.barangay;
        this.city = builder.city;
        this.zipcode = builder.zipcode;
    }

    public Address() {}

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getBarangay() {
        return barangay;
    }

    public String getCity() {
        return city;
    }

    public int getZipcode() {
        return zipcode;
    }

    public static class Builder {
        private String streetNumber;
        private String barangay;
        private String city;
        private int zipcode;

        public Builder streetNumber(String streetNumber) {
            this.streetNumber = streetNumber;
            return this;
        }

        public Builder barangay(String barangay) {
            this.barangay = barangay;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder zipcode(int zipcode) {
            this.zipcode = zipcode;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }
}