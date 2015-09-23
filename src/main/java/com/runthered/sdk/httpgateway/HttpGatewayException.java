package com.runthered.sdk.httpgateway;

public class HttpGatewayException extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HttpGatewayException() {}

    public HttpGatewayException(String message){
            super(message);
    }

}
