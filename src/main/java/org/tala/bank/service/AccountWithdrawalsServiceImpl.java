package org.tala.bank.service;

import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tala.bank.dao.AccountWithdrawalsRepository;
import org.tala.bank.model.AccountWithdrawal;
import org.tala.bank.model.BankAccount;

/**
 * Created by Simon on 14/02/2017.
 */

@Transactional
@Service("accountWithdrawalsService")
public class AccountWithdrawalsServiceImpl implements AccountWithdrawalsService
{

	@Autowired
	AccountWithdrawalsRepository withdrawalRepository;

	@Autowired
	BankAccountsService accountService;

	public Long getTotalWithdrawnAmountForToday(BankAccount account)
	{
		// TODO: use sql SUM() function instead of this loop
		List<AccountWithdrawal> withdrawals = withdrawalRepository.findByBankAccountAndDate(account.getId(), LocalDate.now().toDate());
		long total = 0;
		for (AccountWithdrawal withdrawal : withdrawals)
		{
			total += withdrawal.getWithdrawalAmount();
		}
		return total;
	}

	public Integer getTotalTrxCountForToday(BankAccount account)
	{
		List<AccountWithdrawal> withdrawals = withdrawalRepository.findByBankAccountAndDate(account.getId(), LocalDate.now().toDate());
		return withdrawals.size();
	}

	public AccountWithdrawal executeWithdraw(BankAccount bankAccount, Long withdrawalAmtInCents)
	{
		Long bal = bankAccount.getBalance();
		bankAccount.setBalance(bal - withdrawalAmtInCents);
		accountService.saveBankAccount(bankAccount);
		AccountWithdrawal withdrawal = new AccountWithdrawal(bankAccount, withdrawalAmtInCents);
		return withdrawalRepository.save(withdrawal);
	}

}
