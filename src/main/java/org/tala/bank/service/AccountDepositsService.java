package org.tala.bank.service;

import org.tala.bank.model.AccountDeposit;
import org.tala.bank.model.BankAccount;

/**
 * Created by Simon on 14/02/2017.
 */
public interface AccountDepositsService
{
	public abstract AccountDeposit executedAccountDeposit(BankAccount bankAccount, Long amountInCents);

	public abstract Long getTotalDepositedAmountForToday(BankAccount bankAccount);

	public abstract Integer getTotalDepositTrxsCountForToday(BankAccount bankAccount);
}
