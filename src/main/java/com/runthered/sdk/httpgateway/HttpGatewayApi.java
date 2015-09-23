package com.runthered.sdk.httpgateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class HttpGatewayApi{
	private String username;
	private String password;
	private String serviceKey;
	private String url;
	private String dlrUrl;
	
	public HttpGatewayApi(String username, String password, String serviceKey){
		this(username, password, serviceKey, "https://connect.runthered.com:14004/public_api/sms/gateway/", "https://connect.runthered.com:14004/public_api/sms/dlr/");
	}

	public HttpGatewayApi(String username, String password, String serviceKey, String url, String dlrUrl){
		this.username = username;
		this.password = password;
		this.serviceKey = serviceKey;
		this.url = url;
		this.dlrUrl = dlrUrl;
	}
	
	private HttpURLConnection addBasicAuth(HttpURLConnection conn) throws IOException, MalformedURLException{
		Base64.Encoder encoder = Base64.getEncoder();
		String normalString = this.username+":"+this.password;
		String encoded = encoder.encodeToString(normalString.getBytes(StandardCharsets.UTF_8) );
		conn.setRequestProperty("Authorization", "Basic "+encoded);
			
		return conn;
					
	}
	
	public String pushMessage(String message, String to) throws HttpGatewayException{
		String fromNumber = null;
		String billingCode = null;
		String partnerReference = null;
		return pushMessage(message, to, fromNumber, billingCode, partnerReference);		
	}
				
	public String pushMessage(String message, String to, String fromNumber) throws HttpGatewayException{
		String billingCode = null;
		String partnerReference = null;
		return pushMessage(message, to, fromNumber, billingCode, partnerReference);		
	}
	
	public String pushMessage(String message, String to, String fromNumber, String billingCode, String partnerReference) throws HttpGatewayException{
		HttpURLConnection conn = null;
		try{
						
			String urlParameters = "message=" + URLEncoder.encode(message, "UTF-8") + "&to=" + URLEncoder.encode(to, "UTF-8");
			if (fromNumber != null){
				urlParameters += "&from=" + URLEncoder.encode(fromNumber, "UTF-8");
			}
			if(billingCode != null){
				urlParameters += "&billingCode=" + URLEncoder.encode(billingCode, "UTF-8");
			}
			if(partnerReference != null){
				urlParameters += "&partnerReference=" + URLEncoder.encode(partnerReference, "UTF-8");
			}
			
			URL obj = new URL(this.url + this.serviceKey);
			conn = (HttpURLConnection) obj.openConnection();
			conn.setRequestMethod("POST");
			
			//add request header
			conn.setDoOutput(true);
			
			conn = addBasicAuth(conn);
			
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			
			String line;
		    StringBuffer response = new StringBuffer(); 
		    while((line = br.readLine()) != null) {
		    	response.append(line);
		        response.append('\r');
		    }
		    br.close();
		    return response.toString();
		
		}catch (Exception e) {
			throw new HttpGatewayException(e.getMessage());
	    }finally{
	    	if(conn != null) {
	    		conn.disconnect(); 
	        }
	    }
	}

	public HttpGatewayDlrQueryResponse queryDlr(String msgId) throws HttpGatewayException{
		HttpURLConnection conn = null;
		try{
			String urlParameters = "id=" + URLEncoder.encode(msgId, "UTF-8");
			
			URL obj = new URL(this.dlrUrl + this.serviceKey + '?' + urlParameters);
			conn = (HttpURLConnection) obj.openConnection();
			conn.setRequestMethod("GET");
			
			conn = addBasicAuth(conn);
						
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(br);
									
			String id = (String) jsonObject.get("id");
			String status = (String) jsonObject.get("status");
			String reason = (String) jsonObject.get("reason");
			HttpGatewayDlrQueryResponse httpGatewayDlrQueryResponse = new HttpGatewayDlrQueryResponse(status, reason, id);
			return httpGatewayDlrQueryResponse;
		
		}catch (Exception e) {
			throw new HttpGatewayException(e.getMessage());
	    }finally{
	    	if(conn != null) {
	    		conn.disconnect(); 
	        }
	    }
	}
}
