package com.ditcherj.contextio.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Jonathan Ditcher on 01/04/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponse extends BaseResponse {
	/*
	{
  "id": stringId of the account,
  "username": stringUsername assigned to the account,
  "created": numberUnix timestamp of account creation time,
  "suspended": numberUnix timestamp of account suspension time 0 means not suspended,
  "email_addresses": arrayArray of email addresses for this account,
  "first_name": stringFirst name of account holder,
  "last_name": stringLast name of account holder,
  "password_expired": numberUnix timestamp of user's password expiration. 0 means still valid,
  "sources": arrayList of email accounts where this account gets data from. See sources for details,
  "nb_messages": numberTotal number of messages in all sources of this account,
  "nb_files": numberTotal number of files in all sources of this account
}
	 */

	private String id;
	private String username;
	private String first_name;
	private String last_name;
	private List<String> email_addresses;

	public AccountResponse() {
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

	@Override
	public String toString() {
		return "AccountResponse{" +
				"id='" + id + '\'' +
				", username='" + username + '\'' +
				", first_name='" + first_name + '\'' +
				", last_name='" + last_name + '\'' +
				", email_addresses=" + email_addresses +
				"} " + super.toString();
	}
}
