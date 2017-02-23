package org.tala.bank.service;

import java.util.Map;

import org.tala.bank.model.BankAccount;

/**
 * Created by Simon on 14/02/2017.
 */
public interface BankAccountsService
{
	public abstract Map<String, Object> executeAccountDeposit(Long accountId, Long depositInCents);

	public abstract Map<String, Object> executeAccountWithdraw(Long accountId, Long withdrawalAmountInCents);

	public abstract BankAccount getBankAccountById(Long accountId);

	public abstract BankAccount saveBankAccount(BankAccount bankAccount);
}
