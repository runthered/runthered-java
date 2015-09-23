package com.runthered.sdk.pushapi;

public class PushApiResponse{
        private String status;
        private String msgId;
        private Long id;

        public String getStatus(){
                return this.status;
        }

        public String getMsgId(){
                return this.msgId;
        }

        public Long getId(){
                return this.id;
        }

        public PushApiResponse(String status, String msgId, Long id){
                this.status = status;
                this.msgId = msgId;
                this.id = id;
        }
}
