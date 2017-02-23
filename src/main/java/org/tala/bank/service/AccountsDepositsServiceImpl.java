package org.tala.bank.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tala.bank.dao.AccountDepositsRepository;
import org.tala.bank.model.AccountDeposit;
import org.tala.bank.model.BankAccount;

/**
 * Created by Simon on 14/02/2017.
 */

@Transactional
@Service("accountDepositsService")
public class AccountsDepositsServiceImpl implements AccountDepositsService
{

	@Autowired
	AccountDepositsRepository accountDepositsRepository;

	@Autowired
	BankAccountsService bankAccountsService;

	public AccountDeposit executedAccountDeposit(BankAccount bankAccount, Long amountInCents)
	{
		Long bal = bankAccount.getBalance();
		bankAccount.setBalance(bal + amountInCents);
		bankAccountsService.saveBankAccount(bankAccount);
		AccountDeposit deposit = new AccountDeposit(bankAccount, amountInCents);
		return accountDepositsRepository.save(deposit);
	}

	public Long getTotalDepositedAmountForToday(BankAccount account)
	{

		List<AccountDeposit> deposits = accountDepositsRepository.getAccountDepositsByAccountAndDate(account.getId(), LocalDate.now().toDate());
		long total = 0;
		for (AccountDeposit deposit : deposits)
		{
			total += deposit.getDepositedAmount();
		}
		return total;

		/*- BigDecimal v = accountDepositsRepository.getTotalAccountDepositsByAccountAndDate(account.getId(), LocalDate.now().toDate());*/

		/*- return accountDepositsRepository.getTotalAccountDepositsByAccountAndDate(account.getId(), LocalDate.now().toDate()); */

	}

	public Integer getTotalDepositTrxsCountForToday(BankAccount account)
	{
		List<AccountDeposit> deposits = accountDepositsRepository.getAccountDepositsByAccountAndDate(account.getId(), LocalDate.now().toDate());
		return deposits.size();
	}
}
