package org.tala.bank.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * Created by Simon on 14/02/2017.
 */
public abstract class BaseIntegrationTest extends AbstractTestNGSpringContextTests
{

	protected HttpUtil httpUtil;

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	protected void setupHttpUtil(int port)
	{
		String baseUrl = "http://localhost:" + port;
		httpUtil = new HttpUtil(baseUrl);
	}
}
