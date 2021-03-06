package com.ditcherj.contextio.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Jonathan Ditcher on 01/04/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    private String id;
    private Long created;
    private Long suspended;
    private String username;
    private String first_name;
    private String last_name;
    private List<String> email_addresses;
    private List<Source> sources;

    public Account() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public List<String> getEmail_addresses() {
        return email_addresses;
    }

    public void setEmail_addresses(List<String> email_addresses) {
        this.email_addresses = email_addresses;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getSuspended() {
        return suspended;
    }

    public void setSuspended(Long suspended) {
        this.suspended = suspended;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", created=" + created +
                ", suspended=" + suspended +
                ", username='" + username + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email_addresses=" + email_addresses +
                ", sources=" + sources +
                '}';
    }
}
