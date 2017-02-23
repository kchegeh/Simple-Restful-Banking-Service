package org.tala.bank.test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

/**
 * Created by Simon on 15/02/2017.
 */
public class HttpUtil
{
	String baseUrl;

	public HttpUtil(String baseUrl)
	{
		this.baseUrl = baseUrl;
	}

	public JSONObject doPost(String url, JSONObject body) throws UnsupportedEncodingException
	{
		StringEntity stringEntity = new StringEntity(body.toString());
		stringEntity.setContentType("application/json");
		return doPost(url, stringEntity);
	}

	private JSONObject doPost(String url, HttpEntity entity)
	{
		try
		{
			System.out.println("******************");
			System.out.println("POST to : " + url);
			URL obj = new URL(baseUrl + url);
			HttpURLConnection urlConnection = (HttpURLConnection) obj.openConnection();
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestMethod("POST");
			urlConnection.setConnectTimeout(30000);
			urlConnection.setReadTimeout(30000);

			urlConnection.setRequestProperty("Connection", "Keep-Alive");
			urlConnection.addRequestProperty("Content-length", entity.getContentLength() + "");
			urlConnection.addRequestProperty(entity.getContentType().getName(), entity.getContentType().getValue());
			DataOutputStream dStream = new DataOutputStream(urlConnection.getOutputStream());
			entity.writeTo(dStream);
			dStream.flush();
			dStream.close();

			JSONObject response = readResponse(urlConnection);
			System.out.println("******************");
			return response;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("success", false);
			jsonObject.put("error", "Unknown error");
			return jsonObject;
		}
	}

	private JSONObject readResponse(HttpURLConnection urlConnection) throws IOException
	{
		int statusCode = urlConnection.getResponseCode();

		if (statusCode == HttpURLConnection.HTTP_OK)
		{
			System.out.println("HTTP Response Status OK");
		}
		else
		{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("success", false);
			jsonObject.put("error", "HTTP Response Status " + statusCode);
			return jsonObject;
		}

		InputStream is = urlConnection.getInputStream();
		byte[] data = IOUtils.toByteArray(is);
		String decodedString = new String(data, "UTF-8");
		return new JSONObject(decodedString);
	}
}
