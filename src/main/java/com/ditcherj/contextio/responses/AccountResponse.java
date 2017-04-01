package com.ditcherj.contextio.responses;

import com.ditcherj.contextio.dto.Account;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Jonathan Ditcher on 01/04/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponse extends ContextIOResponse<Account> {

	private Account account;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "AccountResponse{" +
				"account=" + account +
				"} " + super.toString();
	}

	@Override
	public void setPayload(Account payload) {
		this.account = payload;


	}
}
