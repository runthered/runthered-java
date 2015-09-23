package com.runthered.sdk.httpgateway.examples;

import com.runthered.sdk.httpgateway.*;


public class TestHttpGatewayApi{
	public static void main(String[] args) {
		try{
			// first send a message
			String username = "snoop7";
			String password = "snoop7";
			String serviceKey = "snop7";
												
			HttpGatewayApi httpGatewayApi = new HttpGatewayApi(username, password, serviceKey);
			String to = "64212432323";
			String fromNumber = "2059";
			String message = "Hello World!";
			String response = httpGatewayApi.pushMessage(message, to, fromNumber);
			System.out.println("The response is " + response);
			
			// now query a DLR
			String msgId = "55ee4c55e1382352f4de069a";
			HttpGatewayDlrQueryResponse httpGatewayDlrQueryResponse = httpGatewayApi.queryDlr(msgId);
			System.out.println("The status is " + httpGatewayDlrQueryResponse.getStatus());
			System.out.println("The reason is " + httpGatewayDlrQueryResponse.getReasonCode());
		}catch(HttpGatewayException e){
			System.out.println(e.toString());
		}

	}


}
