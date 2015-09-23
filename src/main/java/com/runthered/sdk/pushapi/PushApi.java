package com.runthered.sdk.pushapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class PushApi{
	private String username;
	private String password;
	private String serviceKey;
	private String url;
	
	public PushApi(String username, String password, String serviceKey){
		this(username, password, serviceKey, "https://connect.runthered.com:10443/public_api/service");
	}
	
	public PushApi(String username, String password, String serviceKey, String url){
		this.username = username;
		this.password = password;
		this.serviceKey = serviceKey;
		this.url = url;
	}
	
	private HttpURLConnection initialiseHttpConnection() throws IOException, MalformedURLException{
		URL obj = new URL(this.url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		
		//add request header
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		Base64.Encoder encoder = Base64.getEncoder();
		String normalString = this.username+":"+this.password;
		String encoded = encoder.encodeToString(normalString.getBytes(StandardCharsets.UTF_8) );
		conn.setRequestProperty("Authorization", "Basic "+encoded);
			
		return conn;
					
	}
	
	private JSONObject makeRequest(String input, HttpURLConnection conn) throws IOException, ParseException{
		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();
		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(br);
		return jsonObject;
	}
	
	public PushApiResponse pushMessage(String body, String to, Long pushId) throws PushApiException{
		String from = null;
		return pushMessage(body, to, from, pushId);
		
	}

	public PushApiResponse pushMessage(String body, String to, String from, Long pushId) throws PushApiException{
		HttpURLConnection conn = null;
		try{
			conn = initialiseHttpConnection();
			
			JSONObject inputObj = new JSONObject();
			inputObj.put("jsonrpc", "2.0");
			inputObj.put("method", "sendsms");
			inputObj.put("id", pushId);
			JSONObject innerInputObj = new JSONObject();
			innerInputObj.put("service_key", this.serviceKey);
			innerInputObj.put("to", to);
			innerInputObj.put("body", body);
			if (from != null){
				innerInputObj.put("frm", from);
			}
			inputObj.put("params", innerInputObj);

			String input = inputObj.toString();

			JSONObject jsonObject = makeRequest(input, conn);
			Long id = (Long) jsonObject.get("id");
			JSONObject innerObj = (JSONObject)jsonObject.get("result");
			if(innerObj == null){
				JSONObject innerErrorObj = (JSONObject)jsonObject.get("error");
				String errorMessage = (String)innerErrorObj.get("message");
				//Long errorCode = (Long)innerErrorObj.get("code");
				throw new PushApiException(errorMessage);

			}
			String status = (String) innerObj.get("status");
			String msgId = (String) innerObj.get("msg_id");
			PushApiResponse pushApiResponse = new PushApiResponse(status, msgId, id);

			conn.disconnect();
			return pushApiResponse;
		
		}catch (PushApiException e){
			throw e;			
		}catch (Exception e) {
			throw new PushApiException(e.getMessage());
	    }finally{
	    	if(conn != null) {
	    		conn.disconnect(); 
	        }
	    }
	}

	public DlrQueryResponse queryDlr(String msgId, Long pushId) throws PushApiException{
		HttpURLConnection conn = null;
		try{
			conn = initialiseHttpConnection();
			
			JSONObject inputObj = new JSONObject();
			inputObj.put("jsonrpc", "2.0");
			inputObj.put("method", "querydlr");
			inputObj.put("id", pushId);
			JSONObject innerInputObj = new JSONObject();
			innerInputObj.put("service_key", this.serviceKey);
			innerInputObj.put("msg_id", msgId);
			inputObj.put("params", innerInputObj);

			String input = inputObj.toString();

			JSONObject jsonObject = makeRequest(input, conn);
			Long id = (Long) jsonObject.get("id");
			JSONObject innerObj = (JSONObject)jsonObject.get("result");
			if(innerObj == null){
				JSONObject innerErrorObj = (JSONObject)jsonObject.get("error");
				String errorMessage = (String)innerErrorObj.get("message");
				//Long errorCode = (Long)innerErrorObj.get("code");
				throw new PushApiException(errorMessage);

			}
			String status = (String) innerObj.get("status");
			String reason = (String) innerObj.get("reason");
            DlrQueryResponse dlrQueryResponse = new DlrQueryResponse(status, reason, id);

			conn.disconnect();
			return dlrQueryResponse;
		
		}catch (PushApiException e){
			throw e;			
		}catch (Exception e) {
			throw new PushApiException(e.getMessage());
	    }finally{
	    	if(conn != null) {
	    		conn.disconnect(); 
	        }
	    }
	}
}
