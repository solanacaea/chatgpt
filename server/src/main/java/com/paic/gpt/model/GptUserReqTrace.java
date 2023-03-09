package com.paic.gpt.model;

import com.paic.gpt.model.audit.DateAudit;
import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "t_gpt_user_req_trace")
public class GptUserReqTrace extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer gptStatus;
    private Integer count;

    @Lob
    @NotBlank
    @Column(name = "question", columnDefinition = "Text")
    private String question;
    @Lob
    @Column(name = "answer", columnDefinition = "Text")
    private String answer;
    @NotBlank
    private String user;
    private String sessionId;
    private String msgId;
    private Integer reqTokens;
    private Integer respTokens;
    private Integer totalTokens;
    private Integer timeCost;

    @Builder
    public GptUserReqTrace(Long id, Integer gptStatus, Integer count,
                           @NotBlank String question, String answer,
                           @NotBlank String user, String sessionId, String msgId,
                           Integer reqTokens, Integer respTokens, Integer totalTokens,
                           Integer timeCost) {
        this.id = id;
        this.gptStatus = gptStatus;
        this.count = count;
        this.question = question;
        this.answer = answer;
        this.user = user;
        this.sessionId = sessionId;
        this.msgId = msgId;
        this.reqTokens = reqTokens;
        this.respTokens = respTokens;
        this.totalTokens = totalTokens;
        this.timeCost = timeCost;
    }

    public GptUserReqTrace() {

    }

    public Long getId() {
        return id;
    }

    public Integer getGptStatus() {
        return gptStatus;
    }

    public Integer getCount() {
        return count;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getUser() {
        return user;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getMsgId() {
        return msgId;
    }

    public Integer getReqTokens() {
        return reqTokens;
    }

    public Integer getRespTokens() {
        return respTokens;
    }

    public Integer getTotalTokens() {
        return totalTokens;
    }

    public Integer getTimeCost() {
        return timeCost;
    }
}
