package org.tala.bank.utils;

import java.math.BigDecimal;

/**
 * Created by Simon on 14/02/2017.
 */
public final class CurrencyUtil
{

	private static final BigDecimal HUNDRED_UNIT = new BigDecimal("100");

	public static Long getCurrencyToCentsAmount(BigDecimal amount)
	{
		return amount.multiply(HUNDRED_UNIT).longValue();
	}

	public static BigDecimal getCentsToCurrencyAmount(Long cents)
	{
		return new BigDecimal(cents).movePointLeft(2);
	}
}
