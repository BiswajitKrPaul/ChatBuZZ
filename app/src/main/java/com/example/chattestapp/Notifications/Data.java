package com.example.chattestapp.Notifications;

public class Data {

    private String senderuseruid;
    private String recieveruseruid;
    private String textMessage;
    private String title;
    private int icon;


    public Data(String senderuseruid, String recieveruseruid, String textMessage, String title, int icon) {
        this.senderuseruid = senderuseruid;
        this.recieveruseruid = recieveruseruid;
        this.textMessage = textMessage;
        this.title = title;
        this.icon = icon;
    }

    public String getSenderuseruid() {
        return senderuseruid;
    }

    public void setSenderuseruid(String senderuseruid) {
        this.senderuseruid = senderuseruid;
    }

    public String getRecieveruseruid() {
        return recieveruseruid;
    }

    public void setRecieveruseruid(String recieveruseruid) {
        this.recieveruseruid = recieveruseruid;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
