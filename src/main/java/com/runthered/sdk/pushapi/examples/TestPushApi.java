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
