package com.paic.gpt.model;

import com.paic.gpt.model.audit.DateAudit;

import javax.persistence.*;

@Entity
@Table(name = "t_user_conversation")
public class Conversation extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String conversationId;
    private String conversationName;

    public Conversation(String username, String conversationId) {
        this.username = username;
        this.conversationId = conversationId;
    }

    public Conversation(String username, String conversationId, String conversationName) {
        this.username = username;
        this.conversationId = conversationId;
        this.conversationName = conversationName;
    }

    public Conversation() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }
}
