# runthered-java

A Java Run The Red API helper library.

# Installation

This can be installed using Maven by adding the following XML to your pom.xml file:
	<dependency>
		<groupId>com.runthered.sdk</groupId>
		<artifactId>runthered-java-sdk</artifactId>
		<version>0.0.1</version>
	</dependency>

# Examples

## HTTP Gateway

### Send an MT and query a delivery reciept using a message id
```java
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

```

## Push API

### Send an MT and query a delivery reciept using a message id
```java
package com.runthered.sdk.pushapi.examples;

import com.runthered.sdk.pushapi.*;


public class TestPushApi{
	public static void main(String[] args) {
		try{
			// first send a message
			String username = "testuser";
			String password = "testuser";
			String serviceKey = "82221";
						
			PushApi pushApi = new PushApi(username, password, serviceKey);
			String to = "64212432323";
			String from = "8222";
			String body = "Hello World!";
			Long id = new Long(12345);
            PushApiResponse pushApiResponse = pushApi.pushMessage(body, to, from, id);
			System.out.println("The status is " + pushApiResponse.getStatus());
			System.out.println("The msgId is " + pushApiResponse.getMsgId());
			System.out.println("The Id is " + pushApiResponse.getId());

			// now query a DLR
			String msgId = "55e3c3dae138237f7b02b64f";
			Long dlrId = new Long(1234);
			DlrQueryResponse dlrQueryResponse = pushApi.queryDlr(msgId, dlrId);
			System.out.println("The status is " + dlrQueryResponse.getStatus());
			System.out.println("The reason is " + dlrQueryResponse.getReasonCode());
		}catch(PushApiException e){
			System.out.println(e.toString());
		}

	}


}

```
