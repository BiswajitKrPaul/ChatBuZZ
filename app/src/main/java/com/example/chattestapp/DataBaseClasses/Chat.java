package com.example.chattestapp.DataBaseClasses;

public class Chat {

    private String senderUid;
    private String messageBody;


    public Chat() {
    }

    public Chat(String senderUid, String messageBody) {
        this.senderUid = senderUid;
        this.messageBody = messageBody;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
