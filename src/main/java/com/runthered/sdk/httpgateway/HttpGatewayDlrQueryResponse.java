package com.runthered.sdk.httpgateway;

public class HttpGatewayDlrQueryResponse{
    private String status;
    private String reasonCode;
    private String id;

    public String getStatus(){
            return this.status;
    }

    public String getReasonCode(){
            return this.reasonCode;
    }

    public String getId(){
            return this.id;
    }

    public HttpGatewayDlrQueryResponse(String status, String reasonCode, String id){
            this.status = status;
            this.reasonCode = reasonCode;
            this.id = id;
    }
}

