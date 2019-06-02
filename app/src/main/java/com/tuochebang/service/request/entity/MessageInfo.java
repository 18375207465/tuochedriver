package com.tuochebang.service.request.entity;

import java.io.Serializable;

public class MessageInfo implements Serializable {
    private String content;
    private int isRead;
    private String messageId;
    private String timestamp;
    private String title;

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsRead() {
        return this.isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String toString() {
        return "MessageInfo{timestamp='" + this.timestamp + '\'' + ", title='" + this.title + '\'' + ", content='" + this.content + '\'' + '}';
    }
}
