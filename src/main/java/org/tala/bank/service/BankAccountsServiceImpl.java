package org.tala.bank.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tala.bank.dao.BankAccountsRepository;
import org.tala.bank.model.BankAccount;
import org.tala.bank.utils.Constants.DepositsConstants;
import org.tala.bank.utils.Constants.WithdrawalConstants;

/**
 * Created by Simon on 14/02/2017.
 */

@Transactional
@Service("bankAccountsService")
public class BankAccountsServiceImpl implements BankAccountsService
{

	@Autowired
	BankAccountsRepository accountRepository;

	@Autowired
	AccountDepositsService depositService;

	@Autowired
	AccountWithdrawalsService withdrawalService;

	@Override
	public Map<String, Object> executeAccountDeposit(Long accountId, Long depositInCents)
	{
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("successful", true);
		response.put("message", "Account Deposit succeeded");

		BankAccount account = getBankAccountById(1L);
		Long depositAmountToday = depositService.getTotalDepositedAmountForToday(account);
		Long runningDeposit = depositAmountToday + depositInCents;

		if (depositInCents > DepositsConstants.MAX_TRANSACTION_DEPOSIT_IN_CENTS)
		{
			response.put("successful", false);
			response.put("message", String.format("Deposit Amount exceeds allowed limit.Submitted Amount is %d, max allowed amount is 40000", depositInCents));
			return response;
		}

		if (runningDeposit > DepositsConstants.MAX_DAILY_DEPOSIT_AMT_IN_CENTS)
		{
			response.put("successful", false);
			response.put("message", "Amount exceeds daily deposit limit");
			return response;
		}

		int depositCountToday = depositService.getTotalDepositTrxsCountForToday(account);

		if (depositCountToday >= DepositsConstants.MAX_DAILY_DEPOSITS)
		{
			response.put("successful", false);
			response.put("message", "Exceeded allowed daily deposit frequency");
			return response;
		}
		depositService.executedAccountDeposit(account, depositInCents);
		return response;
	}

	@Override
	public Map<String, Object> executeAccountWithdraw(Long accountId, Long withdrawalAmountInCents)
	{
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("successful", true);
		response.put("message", "Withdraw Transaction successful");

		BankAccount account = getBankAccountById(1L);

		Long totalWithdrawalToday = withdrawalService.getTotalWithdrawnAmountForToday(account);
		Long expectedWithdrawal = totalWithdrawalToday + withdrawalAmountInCents;

		if (expectedWithdrawal > WithdrawalConstants.MAX_DAILY_WITHDRAWAL_AMT_IN_CENTS)
		{
			response.put("successful", false);
			response.put("message", "Withdrawal Amount exceeds allowed daily withdrawal limit");
			return response;
		}

		if (withdrawalAmountInCents > WithdrawalConstants.MAX_TRANSACTION_WITHDRAWAL_IN_CENTS)
		{
			response.put("successful", false);
			response.put("message", "Amount exceeds transaction withdrawal limit");
			return response;
		}

		int withdrawalCountToday = withdrawalService.getTotalTrxCountForToday(account);

		if (withdrawalCountToday >= WithdrawalConstants.MAX_DAILY_WITHDRAWALS)
		{
			response.put("successful", false);
			response.put("message", "Exceeded daily withdrawal frequency");
			return response;
		}

		Long currentBalance = account.getBalance();

		if (withdrawalAmountInCents > currentBalance)
		{
			response.put("successful", false);
			response.put("message", "Withdrawal request failed. Can't withdrawal more than available balance");
			return response;
		}

		withdrawalService.executeWithdraw(account, withdrawalAmountInCents);
		return response;// success
	}

	@Override
	public BankAccount getBankAccountById(Long accountId)
	{
		return accountRepository.findOne(accountId);
	}

	@Override
	public BankAccount saveBankAccount(BankAccount account)
	{
		return accountRepository.save(account);
	}
}
