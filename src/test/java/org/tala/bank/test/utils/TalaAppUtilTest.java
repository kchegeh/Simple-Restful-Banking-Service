package org.tala.bank.test.utils;

import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.tala.bank.utils.TalaAppUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by Simon on 16/02/2017.
 */

public class TalaAppUtilTest extends AbstractTestNGSpringContextTests
{
	@BeforeMethod
	public void setUp() throws Exception
	{

	}

	@Test
	public void testIsEmptyOrNullStringReturnsTrueForEmptyString()
	{
		Assert.assertTrue(TalaAppUtil.isEmptyOrNullString(""));
	}

	@Test
	public void testIsEmptyOrNullStringReturnsTrueForNullString()
	{
		Assert.assertTrue(TalaAppUtil.isEmptyOrNullString(null));
	}

}
