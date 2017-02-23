package org.tala.bank.service;

import org.tala.bank.model.AccountWithdrawal;
import org.tala.bank.model.BankAccount;

/**
 * Created by Simon on 14/02/2017.
 */
public interface AccountWithdrawalsService
{

	public abstract Long getTotalWithdrawnAmountForToday(BankAccount bankAccount);

	public abstract Integer getTotalTrxCountForToday(BankAccount bankAccount);

	public abstract AccountWithdrawal executeWithdraw(BankAccount bankAccount, Long withdrawalAmtInCents);
}
