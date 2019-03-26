package com.example.genericrestaurant;

public class ResponseMessage {

    String textmessage;
    boolean isMe;

    public ResponseMessage(String textmessage, boolean isMe) {
        this.textmessage = textmessage;
        this.isMe = isMe;
    }

    public String getTextmessage() {
        return textmessage;
    }

    public void setTextmessage(String textmessage) {
        this.textmessage = textmessage;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }
}
