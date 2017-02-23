package org.tala.bank.rest.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tala.bank.model.BankAccount;
import org.tala.bank.rest.model.JSONResponse;
import org.tala.bank.service.BankAccountsService;
import org.tala.bank.utils.Constants.EndPoints;
import org.tala.bank.utils.CurrencyUtil;

/**
 * Created by Simon on 14/02/2017.
 */

@RestController
@RequestMapping("rest/account")
public class BankAccountsController
{

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	BankAccountsService bankAccountsService;

	@RequestMapping(value = EndPoints.BALANCE_ENDPOINT, method = RequestMethod.POST)
	public ResponseEntity<JSONResponse> getBalance(@RequestBody String body)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(body);
			String accountId = jsonObject.get("accountId").toString();

			final BankAccount bankAccount = bankAccountsService.getBankAccountById(Long.valueOf(accountId));

			if (bankAccount != null)
			{
				Long balanceInCents = bankAccount.getBalance();
				BigDecimal balance = CurrencyUtil.getCentsToCurrencyAmount(balanceInCents);

				return new ResponseEntity<JSONResponse>(JSONResponse.getSuccessfulOperationResponse("$" + balance), HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<JSONResponse>(JSONResponse.getFailedOperationResponse("No Bank Account found for Account Id"), HttpStatus.OK);
			}

		}
		catch (Exception exc)
		{
			logger.error(exc.getMessage(), exc);
			return new ResponseEntity<JSONResponse>(JSONResponse.getFailedOperationResponse(exc.getMessage()), HttpStatus.OK);
		}
	}

	@RequestMapping(value = EndPoints.DEPOSIT_ENDPOINT, method = RequestMethod.POST)
	public ResponseEntity<JSONResponse> executeAccountDeposit(@RequestBody String body)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(body);

			String accountId = jsonObject.get("accountId").toString();
			String depositAmount = jsonObject.get("depositAmount").toString();

			Long depositInCents = CurrencyUtil.getCurrencyToCentsAmount(new BigDecimal(depositAmount));

			final BankAccount bankAccount = bankAccountsService.getBankAccountById(Long.valueOf(accountId));

			if (bankAccount != null)
			{
				Map<String, Object> response = bankAccountsService.executeAccountDeposit(Long.valueOf(accountId), depositInCents);

				if ((Boolean) response.get("successful"))
				{
					return new ResponseEntity<JSONResponse>(JSONResponse.getSuccessfulOperationResponse(response.get("message")), HttpStatus.OK);
				}

				return new ResponseEntity<JSONResponse>(JSONResponse.getFailedOperationResponse((String) response.get("message")), HttpStatus.OK);

			}
			else
			{
				return new ResponseEntity<JSONResponse>(JSONResponse.getFailedOperationResponse("No Bank Account found for Account Id"), HttpStatus.OK);
			}
		}
		catch (Exception exc)
		{
			logger.error(exc.getMessage(), exc);
			return new ResponseEntity<JSONResponse>(JSONResponse.getFailedOperationResponse(exc.getMessage()), HttpStatus.OK);
		}
	}

	@RequestMapping(value = EndPoints.WITHDRAWAL_ENDPOINT, method = RequestMethod.POST)
	public ResponseEntity<JSONResponse> executeAccountWithdraw(@RequestBody String body)
	{
		try
		{
			JSONObject jsonObject = new JSONObject(body);

			String accountId = jsonObject.get("accountId").toString();
			String withdrawalAmount = jsonObject.get("withdrawalAmount").toString();

			final BankAccount bankAccount = bankAccountsService.getBankAccountById(Long.valueOf(accountId));

			if (bankAccount != null)
			{
				Long cents = CurrencyUtil.getCurrencyToCentsAmount(new BigDecimal(withdrawalAmount));
				Map<String, Object> response = bankAccountsService.executeAccountWithdraw(Long.valueOf(accountId), cents);

				if ((Boolean) response.get("successful"))
				{
					return new ResponseEntity<JSONResponse>(JSONResponse.getSuccessfulOperationResponse(response.get("message")), HttpStatus.OK);
				}

				return new ResponseEntity<JSONResponse>(JSONResponse.getFailedOperationResponse((String) response.get("message")), HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<JSONResponse>(JSONResponse.getFailedOperationResponse("No Bank Account found for Account Id"), HttpStatus.OK);
			}
		}
		catch (Exception exc)
		{
			logger.error(exc.getMessage(), exc);
			return new ResponseEntity<JSONResponse>(JSONResponse.getFailedOperationResponse(exc.getMessage()), HttpStatus.OK);
		}
	}
}
