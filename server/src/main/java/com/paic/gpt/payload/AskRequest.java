package com.paic.gpt.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AskRequest {
    @NotBlank
    @Size(max = 4000)
    private String question;

    private String conversationId;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

}
