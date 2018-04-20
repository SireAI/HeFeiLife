package com.sire.messagepushmodule.Pojo;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/23
 * Author:Sire
 * Description:
 * ==================================================
 */

public class PushInfor {

   public enum PushEvent{
        EVENT_CLIENT_ID,EVENT_SERVER_DATA
    }

    private PushEvent pushEvent;
    private String messageContent;
    private String clientId;
    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public PushEvent getPushEvent() {
        return pushEvent;
    }

    public void setPushEvent(PushEvent pushEvent) {
        this.pushEvent = pushEvent;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }



    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
