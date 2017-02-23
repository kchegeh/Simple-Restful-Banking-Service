package org.tala.bank.utils;

/**
 * Created by Simon on 14/02/2017.
 */
public class Constants
{

	public static class EndPoints
	{
		public static final String BALANCE_ENDPOINT = "/balance";
		public static final String DEPOSIT_ENDPOINT = "/deposit";
		public static final String WITHDRAWAL_ENDPOINT = "/withdrawal";
	}

	public static class DepositsConstants
	{
		public static final Integer MAX_DAILY_DEPOSITS = 4;
		public static final Long MAX_DAILY_DEPOSIT_AMT_IN_CENTS = 15000000L;
		public static final Long MAX_TRANSACTION_DEPOSIT_IN_CENTS = 4000000L;
	}

	public static class WithdrawalConstants
	{
		public static final int MAX_DAILY_WITHDRAWALS = 3;
		public static final Long MAX_DAILY_WITHDRAWAL_AMT_IN_CENTS = 5000000L;
		public static final Long MAX_TRANSACTION_WITHDRAWAL_IN_CENTS = 2000000L;
	}

}
