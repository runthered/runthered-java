package com.runthered.sdk.httpgateway;

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
@PrepareForTest({ HttpGatewayApi.class, URL.class, HttpURLConnection.class })
public class HttpGatewayApiTest extends TestCase{
		
	/**
     * test response.
     */
    private static final String TEST_RESPONSE = "5600eb53e138237e9b37dba4";
    
    /**
     * test dlr query response.
     */
    private static final String TEST_DLR_RESPONSE = "{\"id\":\"5600eb53e138237e9b37dba4\", \"status\":\"ENROUTE\", \"reason\":\"000\"}";
   
    /**
     * test dlr query response.
     */
    private static final String TEST_DLR_ERROR_RESPONSE = "{\"message\": \"Unknown Message Id: Could not find message id 5600eb53e138237e9b37dba4\", \"code\": 404}";

    
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
    private static final String MESSAGEID = "5600eb53e138237e9b37dba4";

    /**
     * URL mock.
     */
    private URL url;
    
    /**
     * DLR URL mock.
     */
    private URL dlrUrl;
    
    /**
     * URL string mock.
     */
    //private URL url;
    private static final String URLSTRING = "http://localhost:10000/public_api/sms/gateway/";
    
    /**
     * dlrURL string mock.
     */
    private static final String DLRURLSTRING = "http://localhost:10000/public_api/sms/dlr/";

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
    private HttpGatewayApi instance;
	
    @Before
	public void setUp() throws Exception{
    	this.url = PowerMockito.mock(URL.class);
    	this.dlrUrl = PowerMockito.mock(URL.class);
        this.connection = PowerMockito.mock(HttpURLConnection.class);

        this.output = new ByteArrayOutputStream();
        this.input = new ByteArrayInputStream(TEST_RESPONSE.getBytes());
        this.instance = new HttpGatewayApi(USERNAME, PASSWORD, SERVICEKEY, URLSTRING, DLRURLSTRING);

        PowerMockito.whenNew(URL.class).withArguments(URLSTRING + SERVICEKEY).thenReturn(this.url);
        PowerMockito.whenNew(URL.class).withArguments(DLRURLSTRING + SERVICEKEY + "?id=" + MESSAGEID).thenReturn(this.dlrUrl);
        PowerMockito.when(this.url.openConnection()).thenReturn(this.connection);
        PowerMockito.when(this.dlrUrl.openConnection()).thenReturn(this.connection);
        PowerMockito.when(this.connection.getResponseCode()).thenReturn(200);
	}
		
	public void testSucessfulMessagePush() throws Exception{
		try{
					
			String to = "64212432323";
			String fromNumber = "2059";
			String message = "Hello World!";
				
			PowerMockito.doReturn(this.connection).when(this.url).openConnection();
	        PowerMockito.doReturn(this.output).when(this.connection).getOutputStream();
	        PowerMockito.doReturn(this.input).when(this.connection).getInputStream();

	        final String response = this.instance.pushMessage(message, to, fromNumber);

	        PowerMockito.verifyNew(URL.class);
	        new URL(URLSTRING + SERVICEKEY);

	        // Mockito.verify(this.url).openConnection(); // cannot be verified (mockito limitation) 
	        Mockito.verify(this.connection).getOutputStream();
	        Mockito.verify(this.connection).setRequestMethod("POST");
	        Mockito.verify(this.connection).setRequestProperty("Authorization", "Basic Ym9iOmJvYmJ5");
	        Mockito.verify(this.connection).setDoOutput(true);
	        Mockito.verify(this.connection).getInputStream();

	        assertEquals(TEST_RESPONSE.replaceAll("\n", "\r") + "\r", response);

		}catch(HttpGatewayException e){
			System.out.println("We failed with Exception " + e);
			fail();
		}
	}
	
	public void testSucessfulMessagePushNoFrom() throws Exception{
		try{
					
			String to = "64212432323";
			String fromNumber = "2059";
			String message = "Hello World!";
				
			PowerMockito.doReturn(this.connection).when(this.url).openConnection();
	        PowerMockito.doReturn(this.output).when(this.connection).getOutputStream();
	        PowerMockito.doReturn(this.input).when(this.connection).getInputStream();

	        final String response = this.instance.pushMessage(message, to);

	        PowerMockito.verifyNew(URL.class);
	        new URL(URLSTRING + SERVICEKEY);

	        // Mockito.verify(this.url).openConnection(); // cannot be verified (mockito limitation) 
	        Mockito.verify(this.connection).getOutputStream();
	        Mockito.verify(this.connection).setRequestMethod("POST");
	        Mockito.verify(this.connection).setRequestProperty("Authorization", "Basic Ym9iOmJvYmJ5");
	        Mockito.verify(this.connection).setDoOutput(true);
	        Mockito.verify(this.connection).getInputStream();

	        assertEquals(TEST_RESPONSE.replaceAll("\n", "\r") + "\r", response);

		}catch(HttpGatewayException e){
			System.out.println("We failed with Exception " + e);
			fail();
		}
	}
	
	public void testSucessfulDlrQuery() throws Exception{
		try{
					
			this.input = new ByteArrayInputStream(TEST_DLR_RESPONSE.getBytes());
			
			PowerMockito.doReturn(this.connection).when(this.dlrUrl).openConnection();
	        PowerMockito.doReturn(this.output).when(this.connection).getOutputStream();
	        PowerMockito.doReturn(this.input).when(this.connection).getInputStream();

	        HttpGatewayDlrQueryResponse httpGatewayDlrQueryResponse = this.instance.queryDlr(MESSAGEID);

	        PowerMockito.verifyNew(URL.class);
	        new URL(DLRURLSTRING + SERVICEKEY + "?id=" + MESSAGEID);

	        // Mockito.verify(this.url).openConnection(); // cannot be verified (mockito limitation) 
	        Mockito.verify(this.connection).setRequestMethod("GET");
	        Mockito.verify(this.connection).setRequestProperty("Authorization", "Basic Ym9iOmJvYmJ5");
	        Mockito.verify(this.connection).getInputStream();

	        assertEquals("000", httpGatewayDlrQueryResponse.getReasonCode());
	        assertEquals("ENROUTE", httpGatewayDlrQueryResponse.getStatus());
	        assertEquals(MESSAGEID, httpGatewayDlrQueryResponse.getId());

		}catch(HttpGatewayException e){
			System.out.println("We failed with Exception " + e);
			fail();
		}
	}
	
	public void testMessagePushUnauthorised() throws Exception{
		try{
					
			String to = "64212432323";
			String fromNumber = "2059";
			String message = "Hello World!";
			
			PowerMockito.when(this.connection.getResponseCode()).thenReturn(401);
				
			PowerMockito.doReturn(this.connection).when(this.url).openConnection();
	        PowerMockito.doReturn(this.output).when(this.connection).getOutputStream();
	        PowerMockito.doReturn(this.input).when(this.connection).getInputStream();

	        this.instance.pushMessage(message, to, fromNumber);

		}catch(HttpGatewayException e){
			assertEquals("Failed : HTTP error code : 401", e.getMessage());
		}
	}
	
	public void testDlrQueryUnauthorised() throws Exception{
		try{
			
			PowerMockito.when(this.connection.getResponseCode()).thenReturn(401);
					
			this.input = new ByteArrayInputStream(TEST_DLR_RESPONSE.getBytes());
			
			PowerMockito.doReturn(this.connection).when(this.dlrUrl).openConnection();
	        PowerMockito.doReturn(this.output).when(this.connection).getOutputStream();
	        PowerMockito.doReturn(this.input).when(this.connection).getInputStream();

	        this.instance.queryDlr(MESSAGEID);
        
		}catch(HttpGatewayException e){
			assertEquals("Failed : HTTP error code : 401", e.getMessage());
		}
	}
	
	public void testMessagePushError() throws Exception{
		try{
					
			String to = "64212432323";
			// missing from number
			String fromNumber = "";
			String message = "Hello World!";
			
			PowerMockito.when(this.connection.getResponseCode()).thenReturn(400);
				
			PowerMockito.doReturn(this.connection).when(this.url).openConnection();
	        PowerMockito.doReturn(this.output).when(this.connection).getOutputStream();
	        PowerMockito.doReturn(this.input).when(this.connection).getInputStream();

	        this.instance.pushMessage(message, to, fromNumber);

		}catch(HttpGatewayException e){
			assertEquals("Failed : HTTP error code : 400", e.getMessage());
		}
	}
	
	public void testDlrQueryError() throws Exception{
		try{
			
			PowerMockito.when(this.connection.getResponseCode()).thenReturn(404);
			// message id not found
			this.input = new ByteArrayInputStream(TEST_DLR_ERROR_RESPONSE.getBytes());
			
			PowerMockito.doReturn(this.connection).when(this.dlrUrl).openConnection();
	        PowerMockito.doReturn(this.output).when(this.connection).getOutputStream();
	        PowerMockito.doReturn(this.input).when(this.connection).getInputStream();

	        this.instance.queryDlr(MESSAGEID);
        
		}catch(HttpGatewayException e){
			assertEquals("Failed : HTTP error code : 404", e.getMessage());
		}
	}

}
