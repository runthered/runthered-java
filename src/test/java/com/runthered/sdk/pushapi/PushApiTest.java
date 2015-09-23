package com.runthered.sdk.pushapi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import junit.framework.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ PushApi.class, URL.class, HttpURLConnection.class })
public class PushApiTest extends TestCase{
		
	/**
     * test response.
     */
    private static final String TEST_RESPONSE = "{\"jsonrpc\": \"2.0\", \"id\": 1, \"result\": {\"status\": \"Accepted\", \"msg_id\": \"515cabc3464af599972c65bc\"}}";
    
    /**
     * test error response.
     */
    private static final String TEST_ERROR_RESPONSE = "{\"jsonrpc\": \"2.0\", \"id\": 12345, \"error\": {\"message\": \"Invalid shortcode.\", \"code\": -1}}";
    
    /**
     * test dlr query response.
     */
    private static final String TEST_DLR_RESPONSE = "{\"jsonrpc\": \"2.0\", \"id\": 1, \"result\": {\"status\": \"DELIVRD\", \"reason\": \"000\", \"msg_id\": \"55c3f9d7e138234bcb3d8b20\"}}";
   
    /**
     * test dlr query response.
     */
    private static final String TEST_DLR_ERROR_RESPONSE = "{\"jsonrpc\": \"2.0\", \"id\": 1, \"error\": {\"message\": \"Unknown Message Id.\", \"code\": -11}}";

    
    /**
     * test username.
     */
    private static final String USERNAME = "bob";
    
    /**
     * test password.
     */
    private static final String PASSWORD = "bobby";
    
    /**
     * test service key.
     */
    private static final String SERVICEKEY = "asklkw";
    
    /**
     * test message id.
     */
    private static final String MESSAGEID = "55c3f9d7e138234bcb3d8b20";

    /**
     * URL mock.
     */
    private URL url;
        
    /**
     * URL string mock.
     */
    //private URL url;
    private static final String URLSTRING = "http://localhost:10000/public_api/service";
    
    /**
     * HttpURLConnection mock.
     */
    private HttpURLConnection connection;

    /**
     * Our output.
     */
    private ByteArrayOutputStream output;

    /**
     * Our input.
     */
    private ByteArrayInputStream input;

    /**
     * Instance under tests.
     */
    private PushApi instance;
	
    @Before
	public void setUp() throws Exception{
    	this.url = PowerMockito.mock(URL.class);
    	this.connection = PowerMockito.mock(HttpURLConnection.class);

        this.output = new ByteArrayOutputStream();
        this.input = new ByteArrayInputStream(TEST_RESPONSE.getBytes());
        this.instance = new PushApi(USERNAME, PASSWORD, SERVICEKEY, URLSTRING);

        PowerMockito.whenNew(URL.class).withArguments(URLSTRING).thenReturn(this.url);
        PowerMockito.when(this.url.openConnection()).thenReturn(this.connection);
        PowerMockito.when(this.connection.getResponseCode()).thenReturn(200);
	}
		
	public void testSucessfulMessagePush() throws Exception{
		try{
					
			String to = "64212432323";
			String fromNumber = "2059";
			String message = "Hello World!";
			Long id = new Long(1);
				
			PowerMockito.doReturn(this.connection).when(this.url).openConnection();
	        PowerMockito.doReturn(this.output).when(this.connection).getOutputStream();
	        PowerMockito.doReturn(this.input).when(this.connection).getInputStream();

	        PushApiResponse pushApiResponse = this.instance.pushMessage(message, to, fromNumber, id);

	        PowerMockito.verifyNew(URL.class);
	        new URL(URLSTRING);

	        // Mockito.verify(this.url).openConnection(); // cannot be verified (mockito limitation) 
	        Mockito.verify(this.connection).getOutputStream();
	        Mockito.verify(this.connection).setRequestMethod("POST");
	        Mockito.verify(this.connection).setRequestProperty("Authorization", "Basic Ym9iOmJvYmJ5");
	        Mockito.verify(this.connection).setRequestProperty("Content-Type", "application/json");
	        Mockito.verify(this.connection).setDoOutput(true);
	        Mockito.verify(this.connection).getInputStream();

	        assertEquals("Accepted", pushApiResponse.getStatus());
	        assertEquals("515cabc3464af599972c65bc", pushApiResponse.getMsgId());
	        assertEquals(new Long(1), pushApiResponse.getId());

		}catch(PushApiException e){
			System.out.println("We failed with Exception " + e);
			fail();
		}
	}
	
	public void testSucessfulMessagePushNoFrom() throws Exception{
		try{
					
			String to = "64212432323";
			String message = "Hello World!";
			Long id = new Long(1);
				
			PowerMockito.doReturn(this.connection).when(this.url).openConnection();
	        PowerMockito.doReturn(this.output).when(this.connection).getOutputStream();
	        PowerMockito.doReturn(this.input).when(this.connection).getInputStream();

	        PushApiResponse pushApiResponse = this.instance.pushMessage(message, to, id);

	        PowerMockito.verifyNew(URL.class);
	        new URL(URLSTRING);

	        // Mockito.verify(this.url).openConnection(); // cannot be verified (mockito limitation) 
	        Mockito.verify(this.connection).getOutputStream();
	        Mockito.verify(this.connection).setRequestMethod("POST");
	        Mockito.verify(this.connection).setRequestProperty("Authorization", "Basic Ym9iOmJvYmJ5");
	        Mockito.verify(this.connection).setRequestProperty("Content-Type", "application/json");
	        Mockito.verify(this.connection).setDoOutput(true);
	        Mockito.verify(this.connection).getInputStream();

	        assertEquals("Accepted", pushApiResponse.getStatus());
	        assertEquals("515cabc3464af599972c65bc", pushApiResponse.getMsgId());
	        assertEquals(new Long(1), pushApiResponse.getId());

		}catch(PushApiException e){
			System.out.println("We failed with Exception " + e);
			fail();
		}
	}
	
	public void testSucessfulDlrQuery() throws Exception{
		try{
					
			this.input = new ByteArrayInputStream(TEST_DLR_RESPONSE.getBytes());
			
			PowerMockito.doReturn(this.connection).when(this.url).openConnection();
	        PowerMockito.doReturn(this.output).when(this.connection).getOutputStream();
	        PowerMockito.doReturn(this.input).when(this.connection).getInputStream();

	        DlrQueryResponse dlrQueryResponse = this.instance.queryDlr(MESSAGEID, new Long(1));

	        PowerMockito.verifyNew(URL.class);
	        new URL(URLSTRING);

	        // Mockito.verify(this.url).openConnection(); // cannot be verified (mockito limitation) 
	        Mockito.verify(this.connection).getOutputStream();
	        Mockito.verify(this.connection).setRequestMethod("POST");
	        Mockito.verify(this.connection).setRequestProperty("Authorization", "Basic Ym9iOmJvYmJ5");
	        Mockito.verify(this.connection).setRequestProperty("Content-Type", "application/json");
	        Mockito.verify(this.connection).setDoOutput(true);
	        Mockito.verify(this.connection).getInputStream();

	        assertEquals("000", dlrQueryResponse.getReasonCode());
	        assertEquals("DELIVRD", dlrQueryResponse.getStatus());
	        assertEquals(new Long(1), dlrQueryResponse.getId());

		}catch(PushApiException e){
			System.out.println("We failed with Exception " + e);
			fail();
		}
	}
	
	public void testMessagePushUnauthorised() throws Exception{
		try{
					
			String to = "64212432323";
			String fromNumber = "2059";
			String message = "Hello World!";
			Long id = new Long(1);
			
			PowerMockito.when(this.connection.getResponseCode()).thenReturn(401);
				
			PowerMockito.doReturn(this.connection).when(this.url).openConnection();
	        PowerMockito.doReturn(this.output).when(this.connection).getOutputStream();
	        PowerMockito.doReturn(this.input).when(this.connection).getInputStream();

	        this.instance.pushMessage(message, to, fromNumber, id);

		}catch(PushApiException e){
			assertEquals("Failed : HTTP error code : 401", e.getMessage());
		}
	}
	
	public void testDlrQueryUnauthorised() throws Exception{
		try{
			
			PowerMockito.when(this.connection.getResponseCode()).thenReturn(401);
					
			this.input = new ByteArrayInputStream(TEST_DLR_RESPONSE.getBytes());
			
			PowerMockito.doReturn(this.connection).when(this.url).openConnection();
	        PowerMockito.doReturn(this.output).when(this.connection).getOutputStream();
	        PowerMockito.doReturn(this.input).when(this.connection).getInputStream();

	        this.instance.queryDlr(MESSAGEID, new Long(1));
        
		}catch(PushApiException e){
			assertEquals("Failed : HTTP error code : 401", e.getMessage());
		}
	}
	
	public void testMessagePushError() throws Exception{
		try{
					
			String to = "64212432323";
			// missing from number
			String fromNumber = "";
			String message = "Hello World!";
			Long id = new Long(1);
			
			this.input = new ByteArrayInputStream(TEST_ERROR_RESPONSE.getBytes());
			
			PowerMockito.when(this.connection.getResponseCode()).thenReturn(200);
				
			PowerMockito.doReturn(this.connection).when(this.url).openConnection();
	        PowerMockito.doReturn(this.output).when(this.connection).getOutputStream();
	        PowerMockito.doReturn(this.input).when(this.connection).getInputStream();

	        this.instance.pushMessage(message, to, fromNumber, id);

		}catch(PushApiException e){
			assertEquals("Invalid shortcode.", e.getMessage());
		}
	}
	
	public void testDlrQueryError() throws Exception{
		try{
			
			PowerMockito.when(this.connection.getResponseCode()).thenReturn(200);
			// message id not found
			this.input = new ByteArrayInputStream(TEST_DLR_ERROR_RESPONSE.getBytes());
			
			PowerMockito.doReturn(this.connection).when(this.url).openConnection();
	        PowerMockito.doReturn(this.output).when(this.connection).getOutputStream();
	        PowerMockito.doReturn(this.input).when(this.connection).getInputStream();

	        this.instance.queryDlr(MESSAGEID, new Long(1));
        
		}catch(PushApiException e){
			assertEquals(e.getMessage(), "Unknown Message Id.");
		}
	}

}
