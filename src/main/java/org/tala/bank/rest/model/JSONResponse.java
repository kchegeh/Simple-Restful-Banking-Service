package org.tala.bank.rest.model;

import org.json.JSONException;
import org.tala.bank.utils.TalaAppUtil;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Simon on 14/02/2017.
 */
public class JSONResponse
{

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object payload;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String errorMessage;

	private boolean successful;

	public JSONResponse()
	{
		super();
	}

	public static JSONResponse getSuccessfulOperationResponse(Object payload)
	{
		if (payload == null)
		{
			throw new JSONException("Value for response payload is required");
		}
		JSONResponse jsonResponse = new JSONResponse();
		jsonResponse.setSuccessful(true);
		jsonResponse.setPayload(payload);
		jsonResponse.setErrorMessage(null);
		return jsonResponse;
	}

	public static JSONResponse getFailedOperationResponse(String errorMessage)
	{
		if (TalaAppUtil.isEmptyOrNullString(errorMessage))
		{
			throw new JSONException("Value for error message is required");
		}

		JSONResponse jsonResponse = new JSONResponse();
		jsonResponse.setSuccessful(false);
		jsonResponse.setPayload(null);
		jsonResponse.setErrorMessage(errorMessage);
		return jsonResponse;
	}

	public boolean isSuccessful()
	{
		return successful;
	}

	public void setSuccessful(boolean success)
	{
		this.successful = success;
	}

	public Object getPayload()
	{
		return payload;
	}

	public void setPayload(Object payload)
	{
		this.payload = payload;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String error)
	{
		this.errorMessage = error;
	}
}
