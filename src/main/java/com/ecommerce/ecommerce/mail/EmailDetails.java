package com.ecommerce.ecommerce.mail;

// Class
public class EmailDetails {

    // Class data members
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;

    // Constructor
    public EmailDetails() {
    }

    // Constructor
    public EmailDetails(String recipient, String msgBody, String subject, String attachment) {
        this.recipient = recipient;
        this.msgBody = msgBody;
        this.subject = subject;
        this.attachment = attachment;
    }

    // Getter
    public String getRecipient() {
        return recipient;
    }

    // Setter
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    // Getter
    public String getMsgBody() {
        return msgBody;
    }

    // Setter
    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    // Getter
    public String getSubject() {
        return subject;
    }

    // Setter
    public void setSubject(String subject) {
        this.subject = subject;
    }

    // Getter
    public String getAttachment() {
        return attachment;
    }

    // Setter
    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

}
