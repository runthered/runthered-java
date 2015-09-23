package com.runthered.sdk.pushapi;

public class DlrQueryResponse{
        private String status;
        private String reasonCode;
        private Long id;

        public String getStatus(){
                return this.status;
        }

        public String getReasonCode(){
                return this.reasonCode;
        }

        public Long getId(){
                return this.id;
        }

        public DlrQueryResponse(String status, String reasonCode, Long id){
                this.status = status;
                this.reasonCode = reasonCode;
                this.id = id;
        }
}
