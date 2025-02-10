package com.ecommercetest.test.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;

public class GoogleUserInfo {

    @JsonProperty("at_hash")
    private String atHash;

    @JsonProperty("sub")
    private String sub;

    @JsonProperty("email_verified")
    private Boolean emailVerified;

    @JsonProperty("iss")
    private String iss;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("nonce")
    private String nonce;

    @JsonProperty("picture")
    private String picture;

    @JsonProperty("aud")
    private List<String> aud;

    @JsonProperty("azp")
    private String azp;

    @JsonProperty("name")
    private String name;

    @JsonProperty("exp")
    private Instant exp;

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("iat")
    private Instant iat;

    @JsonProperty("email")
    private String email;

    // Getters e setters

    public String getAtHash() {
        return atHash;
    }

    public void setAtHash(String atHash) {
        this.atHash = atHash;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<String> getAud() {
        return aud;
    }

    public void setAud(List<String> aud) {
        this.aud = aud;
    }

    public String getAzp() {
        return azp;
    }

    public void setAzp(String azp) {
        this.azp = azp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getExp() {
        return exp;
    }

    public void setExp(Instant exp) {
        this.exp = exp;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Instant getIat() {
        return iat;
    }

    public void setIat(Instant iat) {
        this.iat = iat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}