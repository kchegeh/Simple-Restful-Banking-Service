package org.tala.bank.test.rest.controller;

import org.joda.time.LocalDate;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.tala.bank.test.BaseIntegrationTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by Simon on 14/02/2017.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BankAccountControllerTest extends BaseIntegrationTest
{

	@LocalServerPort
	int port;

	@BeforeMethod
	public void setUp() throws Exception
	{
		setupHttpUtil(this.port);
	}

	@AfterMethod
	public void tearDown() throws Exception
	{

		jdbcTemplate.execute("UPDATE Bank_Accounts SET Balance = 0 where id = 1");
		jdbcTemplate.execute("DELETE FROM Accounts_Withdrawals");
		jdbcTemplate.execute("DELETE FROM Accounts_Deposits");
	}

	@Test
	public void testBalanceEndPointReturnsErrorWhenAccountNotFound() throws Exception
	{
		// set data
		jdbcTemplate.execute("UPDATE Bank_Accounts SET Balance = 100000 where id = 1");

		JSONObject postRequestObject = new JSONObject();
		postRequestObject.put("accountId", 2L);

		JSONObject actualJsonResponse = httpUtil.doPost("/rest/account/balance", postRequestObject);
		System.out.println(actualJsonResponse);

		JSONObject expectedJsonResponse = new JSONObject();
		expectedJsonResponse.put("successful", false);
		expectedJsonResponse.put("errorMessage", "No Bank Account found for Account Id");

		JSONAssert.assertEquals(expectedJsonResponse, actualJsonResponse, true);

	}

	@Test
	public void testBalanceEndPointReturnsCorrectAmount() throws Exception
	{
		// set data
		jdbcTemplate.execute("UPDATE Bank_Accounts SET Balance = 100000 where id = 1");

		JSONObject postRequestObject = new JSONObject();
		postRequestObject.put("accountId", 1L);

		JSONObject actualJsonResponse = httpUtil.doPost("/rest/account/balance", postRequestObject);
		System.out.println(actualJsonResponse);

		JSONObject expectedJsonResponse = new JSONObject();
		expectedJsonResponse.put("successful", true);
		expectedJsonResponse.put("payload", "$1000.00");

		JSONAssert.assertEquals(expectedJsonResponse, actualJsonResponse, true);

	}

	@Test
	public void testAccountDepositReturnsErrorMessageWhenAccountNotFound() throws Exception
	{
		JSONObject postRequestObject = new JSONObject();
		postRequestObject.put("accountId", 2L);
		postRequestObject.put("depositAmount", 1000);
		JSONObject response = httpUtil.doPost("/rest/account/deposit", postRequestObject);
		System.out.println(response);

		JSONObject expectedJsonResponse = new JSONObject();
		expectedJsonResponse.put("successful", false);
		expectedJsonResponse.put("errorMessage", "No Bank Account found for Account Id");

		JSONAssert.assertEquals(expectedJsonResponse, response, true);

	}

	@Test
	public void testAccountDepositWorksCorrectly() throws Exception
	{
		JSONObject postRequestObject = new JSONObject();
		postRequestObject.put("accountId", 1L);
		postRequestObject.put("depositAmount", 1000);
		JSONObject response = httpUtil.doPost("/rest/account/deposit", postRequestObject);
		System.out.println(response);

		JSONObject expectedJsonResponse = new JSONObject();
		expectedJsonResponse.put("successful", true);
		expectedJsonResponse.put("payload", "Account Deposit succeeded");

		JSONAssert.assertEquals(expectedJsonResponse, response, true);

		postRequestObject = new JSONObject();
		postRequestObject.put("accountId", 1L);

		response = httpUtil.doPost("/rest/account/balance", postRequestObject);
		System.out.println(response);

		expectedJsonResponse = new JSONObject();
		expectedJsonResponse.put("successful", true);
		expectedJsonResponse.put("payload", "$1000.00");

		JSONAssert.assertEquals(expectedJsonResponse, response, true);

	}

	@Test
	public void testAccountDepositFailsWhenMaxDailyLimitReached() throws Exception
	{
		// set deposits to 150k, each deposit not more than 40k
		String sql = "INSERT INTO Accounts_Deposits (Account_Id,Deposited_Amount,Date_Deposited,Created_By,Created_Date) values (?,?,?,?,?)";
		jdbcTemplate.update(sql, new Object[] { 1, 4000000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });
		jdbcTemplate.update(sql, new Object[] { 1, 4000000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });
		jdbcTemplate.update(sql, new Object[] { 1, 4000000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });
		jdbcTemplate.update(sql, new Object[] { 1, 3000000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });
		jdbcTemplate.update("UPDATE Bank_Accounts SET Balance = 2000000 WHERE Id = 1");

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("accountId", 1l);
		jsonObject.put("depositAmount", 1000);
		JSONObject actualJsonResponse = httpUtil.doPost("/rest/account/deposit", jsonObject);

		JSONObject expectedJsonResponse = new JSONObject();
		expectedJsonResponse.put("successful", false);
		expectedJsonResponse.put("errorMessage", "Amount exceeds daily deposit limit");

		JSONAssert.assertEquals(expectedJsonResponse, actualJsonResponse, true);
	}

	@Test
	public void testDepositFailsWhenDepositedAmountWillExceedDailyLimit() throws Exception
	{
		// set deposits to 120k, each deposit not more than 40k
		String sql = "INSERT INTO Accounts_Deposits (Account_Id,Deposited_Amount,Date_Deposited,Created_By,Created_Date) values (?,?,?,?,?)";
		jdbcTemplate.update(sql, new Object[] { 1, 4000000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });
		jdbcTemplate.update(sql, new Object[] { 1, 4000000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });
		jdbcTemplate.update(sql, new Object[] { 1, 4000000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });
		jdbcTemplate.update("UPDATE Bank_Accounts SET Balance = 2000000 WHERE Id = 1");

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("accountId", 1l);
		jsonObject.put("depositAmount", 40000);

		JSONObject actualJsonResponse = httpUtil.doPost("/rest/account/deposit", jsonObject);
		System.out.println(actualJsonResponse);

		JSONObject expectedJsonResponse = new JSONObject();
		expectedJsonResponse.put("successful", false);
		expectedJsonResponse.put("errorMessage", "Amount exceeds daily deposit limit");

		JSONAssert.assertEquals(expectedJsonResponse, actualJsonResponse, true);
	}

	@Test
	public void testAccountDepositFailsWhenDepositAmountExceeds40k() throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("accountId", 1L);
		jsonObject.put("depositAmount", 40001);
		JSONObject response = httpUtil.doPost("/rest/account/deposit", jsonObject);
		System.out.println(response);

		JSONObject expectedJson = new JSONObject();
		expectedJson.put("successful", false);
		expectedJson.put("errorMessage", "Deposit Amount exceeds allowed limit.Submitted Amount is 4000100, max allowed amount is 40000");

		JSONAssert.assertEquals(expectedJson, response, true);
	}

	@Test
	public void testDepositFailsAfterFourTransactionsForTheDay() throws Exception
	{
		String sql = "INSERT INTO Accounts_Deposits (Account_Id,Deposited_Amount,Date_Deposited,Created_By,Created_Date) values (?,?,?,?,?)";

		jdbcTemplate.update(sql, new Object[] { 1, 1000000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });
		jdbcTemplate.update(sql, new Object[] { 1, 1000000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });
		jdbcTemplate.update(sql, new Object[] { 1, 1000000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });
		jdbcTemplate.update(sql, new Object[] { 1, 1000000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });
		jdbcTemplate.update("UPDATE Bank_Accounts SET Balance = 4000000 WHERE Id = 1");

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("accountId", 1l);
		jsonObject.put("depositAmount", 40000);

		JSONObject actualJsonResponse = httpUtil.doPost("/rest/account/deposit", jsonObject);
		System.out.println(actualJsonResponse);

		JSONObject expectedJsonResponse = new JSONObject();
		expectedJsonResponse.put("successful", false);
		expectedJsonResponse.put("errorMessage", "Exceeded allowed daily deposit frequency");

		JSONAssert.assertEquals(expectedJsonResponse, actualJsonResponse, true);
	}

	@Test
	public void testAccountWithdrawalReturnsErrorWhenAccountNotFound() throws Exception
	{
		String sql1 = "INSERT INTO Accounts_Deposits (Account_Id,Deposited_Amount,Date_Deposited,Created_By,Created_Date) values (?,?,?,?,?)";
		jdbcTemplate.update(sql1, new Object[] { 1, 10000000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });

		jdbcTemplate.update("UPDATE Bank_Accounts SET Balance = 10000000 WHERE Id = 1");

		JSONObject postRequestObject = new JSONObject();

		postRequestObject.put("accountId", 2l);
		postRequestObject.put("withdrawalAmount", 1000);

		JSONObject actualJsonResponse = httpUtil.doPost("/rest/account/withdrawal", postRequestObject);
		System.out.println(actualJsonResponse);

		JSONObject expectedJsonResponse = new JSONObject();
		expectedJsonResponse.put("successful", false);
		expectedJsonResponse.put("errorMessage", "No Bank Account found for Account Id");

	}

	@Test
	public void testWithdrawSuccess() throws Exception
	{
		String sql1 = "INSERT INTO Accounts_Deposits (Account_Id,Deposited_Amount,Date_Deposited,Created_By,Created_Date) values (?,?,?,?,?)";
		jdbcTemplate.update(sql1, new Object[] { 1, 10000000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });

		jdbcTemplate.update("UPDATE Bank_Accounts SET Balance = 10000000 WHERE Id = 1");

		JSONObject postRequestObject = new JSONObject();

		postRequestObject.put("accountId", 1l);
		postRequestObject.put("withdrawalAmount", 1000);

		JSONObject actualJsonResponse = httpUtil.doPost("/rest/account/withdrawal", postRequestObject);
		System.out.println(actualJsonResponse);

		JSONObject expectedJsonResponse = new JSONObject();
		expectedJsonResponse.put("successful", true);
		expectedJsonResponse.put("payload", "Withdraw Transaction successful");

		JSONAssert.assertEquals(expectedJsonResponse, actualJsonResponse, true);

		postRequestObject = new JSONObject();
		postRequestObject.put("accountId", 1L);

		actualJsonResponse = httpUtil.doPost("/rest/account/balance", postRequestObject);
		System.out.println(actualJsonResponse);

		expectedJsonResponse = new JSONObject();
		expectedJsonResponse.put("successful", true);
		expectedJsonResponse.put("payload", "$99000.00");

		JSONAssert.assertEquals(expectedJsonResponse, actualJsonResponse, true);
	}

	@Test
	public void testWithdrawFailsWhenDailyLimitReached() throws Exception
	{
		String sql1 = "INSERT INTO Accounts_Deposits (Account_Id,Deposited_Amount,Date_Deposited,Created_By,Created_Date) values (?,?,?,?,?)";
		jdbcTemplate.update(sql1, new Object[] { 1, 7000000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });

		String sql = "INSERT INTO Accounts_withdrawals (Account_Id,Withdrawal_Amount,Withdrawal_Date,Created_By,Created_Date) values (?,?,?,?,?)";
		jdbcTemplate.update(sql, new Object[] { 1, 5000000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });

		jdbcTemplate.update("UPDATE Bank_Accounts SET Balance = 2000000 WHERE Id = 1");

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("accountId", 1L);
		jsonObject.put("withdrawalAmount", 1000000);

		JSONObject actualJsonResponse = httpUtil.doPost("/rest/account/withdrawal", jsonObject);
		System.out.println(actualJsonResponse);

		JSONObject expectedJsonResponse = new JSONObject();
		expectedJsonResponse.put("successful", false);
		expectedJsonResponse.put("errorMessage", "Withdrawal Amount exceeds allowed daily withdrawal limit");

		JSONAssert.assertEquals(expectedJsonResponse, actualJsonResponse, true);
	}

	@Test
	public void testWithdrawFailsWhenLimitPerTransactionReached() throws Exception
	{
		String sql1 = "INSERT INTO Accounts_Deposits (Account_Id,Deposited_Amount,Date_Deposited,Created_By,Created_Date) values (?,?,?,?,?)";

		jdbcTemplate.update(sql1, new Object[] { 1, 40000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });

		jdbcTemplate.update("UPDATE Bank_Accounts SET Balance = 40000 WHERE Id = 1");

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("accountId", 1l);
		jsonObject.put("withdrawalAmount", 20001);

		JSONObject actualJsonResponse = httpUtil.doPost("/rest/account/withdrawal", jsonObject);
		System.out.println(actualJsonResponse);

		JSONObject expectedJsonResponse = new JSONObject();
		expectedJsonResponse.put("successful", false);
		expectedJsonResponse.put("errorMessage", "Amount exceeds transaction withdrawal limit");

		JSONAssert.assertEquals(expectedJsonResponse, actualJsonResponse, true);
	}

	@Test
	public void testWithdrawRequestFailsAfter3SucessfulTransactionsOnSameDay() throws Exception
	{
		String sql1 = "INSERT INTO Accounts_Deposits (Account_Id,Deposited_Amount,Date_Deposited,Created_By,Created_Date) values (?,?,?,?,?)";

		jdbcTemplate.update(sql1, new Object[] { 1, 60000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });

		String sql = "INSERT INTO Accounts_withdrawals (Account_Id,Withdrawal_Amount,Withdrawal_Date,Created_By,Created_Date) values (?,?,?,?,?)";

		jdbcTemplate.update(sql, new Object[] { 1, 20000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });
		jdbcTemplate.update(sql, new Object[] { 1, 40000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });
		jdbcTemplate.update(sql, new Object[] { 1, 60000, new LocalDate().toDate(), "User 1", new LocalDate().toDate() });

		jdbcTemplate.update("UPDATE Bank_Accounts SET Balance = 48000 WHERE Id = 1");

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("accountId", 1l);
		jsonObject.put("withdrawalAmount", 5000);

		JSONObject actualJsonResponse = httpUtil.doPost("/rest/account/withdrawal", jsonObject);
		System.out.println(actualJsonResponse);

		JSONObject expectedJsonResponse = new JSONObject();
		expectedJsonResponse.put("successful", false);
		expectedJsonResponse.put("errorMessage", "Exceeded daily withdrawal frequency");

		JSONAssert.assertEquals(expectedJsonResponse, actualJsonResponse, true);
	}

	@Test
	public void testAccountWithdrawFailsWhenRequestedAmountExceedsAvailableBalance() throws Exception
	{
		jdbcTemplate.update("UPDATE Bank_Accounts SET Balance = 10000 WHERE Id = 1");

		JSONObject postRequestObj = new JSONObject();
		postRequestObj.put("accountId", 1L);
		postRequestObj.put("withdrawalAmount", 10001);

		JSONObject actualJsonResponse = httpUtil.doPost("/rest/account/withdrawal", postRequestObj);
		System.out.println(actualJsonResponse);

		JSONObject expectedJsonResponse = new JSONObject();
		expectedJsonResponse.put("successful", false);
		expectedJsonResponse.put("errorMessage", "Withdrawal request failed. Can't withdrawal more than available balance");

		JSONAssert.assertEquals(expectedJsonResponse, actualJsonResponse, true);
	}
}