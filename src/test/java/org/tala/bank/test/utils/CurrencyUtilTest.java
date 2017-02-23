package org.tala.bank.test.utils;

import java.math.BigDecimal;

import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.tala.bank.utils.CurrencyUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by Simon on 15/02/2017.
 */

public class CurrencyUtilTest extends AbstractTestNGSpringContextTests
{
	@BeforeMethod
	public void setUp() throws Exception
	{

	}

	@Test
	public void textGetCurrencyToCentsAmountReturnCorrectAmountInCents()
	{
		Assert.assertTrue(1000000l == CurrencyUtil.getCurrencyToCentsAmount(new BigDecimal(10000l)));
	}

	@Test
	public void textGetCentsToCurrencyAmountAmountInDollars()
	{
		Assert.assertTrue(new BigDecimal(1000000l).movePointLeft(2).equals(CurrencyUtil.getCentsToCurrencyAmount(1000000l)));
	}
}
