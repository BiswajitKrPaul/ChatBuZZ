package com.example.chattestapp.DataBaseClasses;

public class Chat {

    private String senderUid;
    private String messageBody;
    private boolean isseen;
    private String chatid;
    private long delivertime;
    private long seentime;


    public Chat() {
    }

    public Chat(String senderUid, String messageBody, boolean isseen) {
        this.senderUid = senderUid;
        this.messageBody = messageBody;
        this.isseen = isseen;
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

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public long getDelivertime() {
        return delivertime;
    }

    public void setDelivertime(long delivertime) {
        this.delivertime = delivertime;
    }

    public long getSeentime() {
        return seentime;
    }

    public void setSeentime(long seentime) {
        this.seentime = seentime;
    }
}
